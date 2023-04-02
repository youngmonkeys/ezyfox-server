package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.*;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzyAbstractHandlerGroup<D extends EzyDestroyable>
    extends EzyLoggable
    implements
    EzyImmediateDeliver,
    EzyDroppedPackets,
    EzyDestroyable {

    protected final EzyChannel channel;

    protected final D decoder;
    protected final EzyNioDataHandler handler;

    protected final boolean streamingEnable;
    protected final AtomicInteger sessionCount;
    protected final EzySessionStats sessionStats;
    protected final EzyNetworkStats networkStats;

    protected final ExecutorService statsThreadPool;

    protected final EzyNioSession session;
    protected final EzySocketStreamQueue streamQueue;
    protected final EzySessionTicketsQueue sessionTicketsQueue;
    protected final EzySocketDisconnectionQueue disconnectionQueue;
    protected final EzySessionTicketsRequestQueues sessionTicketsRequestQueues;

    public EzyAbstractHandlerGroup(Builder builder) {
        this.session = builder.session;
        this.sessionCount = builder.sessionCount;
        this.sessionStats = builder.sessionStats;
        this.networkStats = builder.networkStats;
        this.statsThreadPool = builder.statsThreadPool;
        this.streamQueue = builder.streamQueue;
        this.streamingEnable = builder.isStreamingEnable();
        this.disconnectionQueue = builder.disconnectionQueue;
        this.sessionTicketsQueue = builder.sessionTicketsQueue;
        this.sessionTicketsRequestQueues = builder.sessionTicketsRequestQueues;

        this.channel = session.getChannel();

        this.decoder = newDecoder(builder.decoder);
        this.handler = newDataHandler(builder);

        ((EzyAbstractSession) session).setDisconnectionQueue(disconnectionQueue);
        ((EzyAbstractSession) session).setSessionTicketsQueue(sessionTicketsQueue);
        ((EzyAbstractSession) session).setDroppedPackets(this);
        ((EzyAbstractSession) session).setImmediateDeliver(this);
        sessionStats.addSessions(1);
        sessionStats.setCurrentSessions(sessionCount.incrementAndGet());
    }

    protected abstract D newDecoder(Object decoder);

    private EzyNioDataHandler newDataHandler(Builder builder) {
        return new EzySimpleNioDataHandler(
            builder.serverContext,
            builder.session
        );
    }

    public final void enqueueDisconnection(EzyConstant reason) {
        session.disconnect(reason);
    }

    public final void fireChannelInactive() {
        fireChannelInactive(EzyDisconnectReason.UNKNOWN);
    }

    public final void fireChannelInactive(EzyConstant reason) {
        try {
            handler.channelInactive(reason);
        } catch (Exception e) {
            logger.error("handler inactive error", e);
        } finally {
            sessionStats.setCurrentSessions(sessionCount.decrementAndGet());
        }
    }

    public final void fireExceptionCaught(Throwable throwable) {
        try {
            handler.exceptionCaught(throwable);
        } catch (Exception e) {
            fireChannelInactive(EzyDisconnectReason.SERVER_ERROR);
        }
    }

    public final void firePacketSend(EzyPacket packet, Object writeBuffer) {
        executeSendingPacket(packet, writeBuffer);
    }

    public final void fireChannelRead(EzyCommand cmd, EzyArray msg) throws Exception {
        handler.channelRead(cmd, msg);
    }

    public void fireStreamBytesReceived(byte[] bytes) throws Exception {
        handler.streamingReceived(bytes);
    }

    @Override
    public final void sendPacketNow(EzyPacket packet) {
        try {
            doSendPacketNow(packet);
        } finally {
            packet.release();
        }
    }

    protected void doSendPacketNow(EzyPacket packet) {
        executeSendingPacket(packet, null);
    }

    @Override
    public void addDroppedPacket(EzyPacket packet) {
        networkStats.addDroppedOutPackets(1);
        networkStats.addDroppedOutBytes(packet.getSize());
    }

    protected final void handleReceivedData(Object data, int dataSize) {
        boolean hasMaxRequestPerSecond = session.addReceivedRequests(1);
        if (hasMaxRequestPerSecond) {
            handler.processMaxRequestPerSecond();
            networkStats.addDroppedInPackets(1);
            networkStats.addDroppedInBytes(dataSize);
            logger.debug("request queue of session: {} is full, drop incoming request", session);
            return;
        }
        if (!session.isActivated()) {
            logger.debug("session: {} maybe destroyed, drop incoming request", session);
            return;
        }
        EzySocketRequest request = newSocketRequest(data);
        boolean success = sessionTicketsRequestQueues.addRequest(request);
        if (!success) {
            networkStats.addDroppedInPackets(1);
            networkStats.addDroppedInBytes(dataSize);
            logger.info("request queue is full, drop incoming request");
        }
    }

    private EzySocketRequest newSocketRequest(Object data) {
        return new EzySimpleSocketRequest(session, (EzyArray) data);
    }

    protected final void executeSendingPacket(EzyPacket packet, Object writeBuffer) {
        try {
            EzyChannel channel = session.getChannel();
            if (canWriteBytes(channel)) {
                EzyConstant transportType = packet.getTransportType();
                if (transportType == EzyTransportType.UDP_OR_TCP) {
                    transportType = session.getDatagramChannelPool() != null
                        ? EzyTransportType.UDP
                        : EzyTransportType.TCP;
                }
                int writeBytes = transportType == EzyTransportType.TCP
                    ? writePacketToSocket(packet, writeBuffer)
                    : writeUdpPacketToSocket(packet, writeBuffer);
                executeAddWrittenBytes(writeBytes);
            }
        } catch (Exception e) {
            int packetSize = packet.getSize();
            networkStats.addWriteErrorPackets(1);
            networkStats.addWriteErrorBytes(packetSize);
            logger.warn(
                "can't send {} bytes to session: {}, error: {}({})",
                packetSize,
                session,
                e.getClass().getName(),
                e.getMessage()
            );
        }
    }

    protected int writePacketToSocket(EzyPacket packet, Object writeBuffer) throws Exception {
        try {
            Object bytes = packet.getData();
            int writeBytes = channel.write(bytes, packet.isBinary());
            packet.release();
            return writeBytes;
        } finally {
            packet.release();
        }
    }

    protected int writeUdpPacketToSocket(EzyPacket packet, Object writeBuffer) throws Exception {
        try {
            EzyDatagramChannelPool udpChannelPool = session.getDatagramChannelPool();
            if (udpChannelPool == null) {
                return 0;
            }
            SocketAddress clientAddress = session.getUdpClientAddress();
            if (clientAddress == null) {
                return 0;
            }
            byte[] bytes = getBytesToWrite(packet);
            int bytesToWrite = bytes.length;
            ByteBuffer buffer = getWriteBuffer((ByteBuffer) writeBuffer, bytesToWrite);
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            DatagramChannel channel = udpChannelPool.getChannel();
            return channel.send(buffer, clientAddress);
        } finally {
            packet.release();
        }
    }

    protected byte[] getBytesToWrite(EzyPacket packet) {
        return (byte[]) packet.getData();
    }

    protected ByteBuffer getWriteBuffer(ByteBuffer fixed, int bytesToWrite) {
        return bytesToWrite > fixed.capacity() ? ByteBuffer.allocate(bytesToWrite) : fixed;
    }

    private boolean canWriteBytes(EzyChannel channel) {
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }

    protected final void executeAddReadBytes(int bytes) {
        statsThreadPool.execute(() -> addReadBytes(bytes));
    }

    private void executeAddWrittenBytes(int bytes) {
        statsThreadPool.execute(() -> addWrittenBytes(bytes));
    }

    private void addReadBytes(int count) {
        try {
            session.addReadBytes(count);
            networkStats.addReadBytes(count);
            networkStats.addReadPackets(1);
        } catch (Throwable e) {
            logger.info("add ready bytes error", e);
        }
    }

    private void addWrittenBytes(int count) {
        try {
            session.addWrittenBytes(count);
            networkStats.addWrittenBytes(count);
            networkStats.addWrittenPackets(1);
        } catch (Throwable e) {
            logger.info("add written bytes error", e);
        }
    }

    public EzyNioSession getSession() {
        return session;
    }

    @Override
    public void destroy() {
        processWithLogException(decoder::destroy);
        processWithLogException(handler::destroy);
    }

    public abstract static class Builder implements EzyBuilder<EzyHandlerGroup> {

        protected AtomicInteger sessionCount;
        protected EzySessionStats sessionStats;
        protected EzyNetworkStats networkStats;

        protected ExecutorService statsThreadPool;

        protected Object decoder;
        protected EzyNioSession session;
        protected EzyServerContext serverContext;
        protected EzySocketStreamQueue streamQueue;
        protected EzySessionTicketsQueue sessionTicketsQueue;
        protected EzySocketDisconnectionQueue disconnectionQueue;
        protected EzySessionTicketsRequestQueues sessionTicketsRequestQueues;

        public Builder decoder(Object decoder) {
            this.decoder = decoder;
            return this;
        }

        public Builder session(EzyNioSession session) {
            this.session = session;
            return this;
        }

        public Builder sessionCount(AtomicInteger sessionCount) {
            this.sessionCount = sessionCount;
            return this;
        }

        public Builder sessionStats(EzySessionStats sessionStats) {
            this.sessionStats = sessionStats;
            return this;
        }

        public Builder networkStats(EzyNetworkStats networkStats) {
            this.networkStats = networkStats;
            return this;
        }

        public Builder statsThreadPool(ExecutorService statsThreadPool) {
            this.statsThreadPool = statsThreadPool;
            return this;
        }

        public Builder serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return this;
        }

        public Builder streamQueue(EzySocketStreamQueue streamQueue) {
            this.streamQueue = streamQueue;
            return this;
        }

        public Builder sessionTicketsQueue(EzySessionTicketsQueue queue) {
            this.sessionTicketsQueue = queue;
            return this;
        }

        public Builder disconnectionQueue(EzySocketDisconnectionQueue disconnectionQueue) {
            this.disconnectionQueue = disconnectionQueue;
            return this;
        }

        public Builder sessionTicketsRequestQueues(EzySessionTicketsRequestQueues sessionTicketsRequestQueues) {
            this.sessionTicketsRequestQueues = sessionTicketsRequestQueues;
            return this;
        }

        protected boolean isStreamingEnable() {
            return serverContext.getServer().getSettings().getStreaming().isEnable();
        }
    }
}
