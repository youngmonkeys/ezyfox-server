package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyCoreConstants;
import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioHandlerGroup;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import org.eclipse.jetty.websocket.api.Session;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

public class EzySocketDataReceiver
    extends EzyLoggable
    implements EzyDestroyable {

    protected final int threadPoolSize;
    protected final ByteBuffer[] tcpByteBuffers;
    protected final ExecutorService[] executorServices;
    protected final EzyHandlerGroupManager handlerGroupManager;

    public EzySocketDataReceiver(Builder builder) {
        this.threadPoolSize = builder.threadPoolSize;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.tcpByteBuffers = newTcpByteBuffers(threadPoolSize);
        this.executorServices = newExecutorServices(threadPoolSize);
    }

    public static Builder builder() {
        return new Builder();
    }

    protected ByteBuffer[] newTcpByteBuffers(int size) {
        ByteBuffer[] answer = new ByteBuffer[size];
        for (int i = 0; i < size; ++i) {
            answer[i] = ByteBuffer.allocateDirect(getMaxBufferSize());
        }
        return answer;
    }

    private ExecutorService[] newExecutorServices(int threadPoolSize) {
        ExecutorService[] answer = new ExecutorService[threadPoolSize];
        for (int i = 0; i < threadPoolSize; ++i) {
            answer[i] = EzyExecutors.newSingleThreadExecutor("socket-data-receiver");
        }
        return answer;
    }

    public void tcpReceive(SocketChannel channel) {
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        ByteBuffer buffer = tcpByteBuffers[index];
        ExecutorService executorService = executorServices[index];
        executorService.execute(() -> doTcpReceive(channel, buffer));
    }

    private void doTcpReceive(SocketChannel channel, ByteBuffer buffer) {
        try {
            tcpReadBytes(channel, buffer);
        } catch (Throwable e) {
            logger.info(
                "I/O error at tcp-data-reader (channel: {})",
                channel,
                e
            );
        }
    }

    protected void tcpReadBytes(
        SocketChannel channel,
        ByteBuffer buffer
    ) throws Throwable {
        int readBytes = -1;
        Throwable exception = null;
        try {
            buffer.clear();
            readBytes = channel.read(buffer);
            if (readBytes > 0) {
                processTcpReadBytes(channel, buffer);
            }
        } catch (EzyConnectionCloseException e) {
            readBytes = -1;
            exception = e;
        } catch (Throwable e) {
            exception = e;
        }
        if (readBytes == -1) {
            tcpCloseConnection(channel);
        }
        if (exception != null) {
            throw exception;
        }
    }

    private void processTcpReadBytes(
        SocketChannel channel,
        ByteBuffer buffer
    ) throws Exception {
        EzyNioHandlerGroup handlerGroup =
            handlerGroupManager.getHandlerGroup(channel);
        if (handlerGroup == null) {
            return;
        }
        buffer.flip();
        byte[] binary = readTcpBytesFromBuffer(
            handlerGroup.getChannel(),
            buffer
        );
        handlerGroup.fireBytesReceived(binary);
    }

    protected byte[] readTcpBytesFromBuffer(
        EzyChannel channel,
        ByteBuffer buffer
    ) throws Exception {
        byte[] binary = new byte[buffer.limit()];
        buffer.get(binary);
        return binary;
    }

    private void tcpCloseConnection(SocketChannel channel) {
        EzyHandlerGroup handlerGroup =
            handlerGroupManager.getHandlerGroup(channel);
        if (handlerGroup != null) {
            handlerGroup.enqueueDisconnection();
        }
    }

    public void udpReceive(Object channel, EzyMessage message) {
        ExecutorService executorService =
            selectedExecutorService(channel);
        executorService.execute(() -> doUdpReceive(channel, message));
    }

    private void doUdpReceive(Object channel, EzyMessage message) {
        try {
            EzyNioHandlerGroup handlerGroup =
                handlerGroupManager.getHandlerGroup(channel);
            if (handlerGroup != null) {
                handlerGroup.fireMessageReceived(message);
            }
        } catch (Throwable e) {
            logger.info(
                "I/O error at udp-message-received (channel: {})",
                channel,
                e
            );
        }

    }

    public void wsReceive(Session session, String message) {
        ExecutorService executorService = selectedExecutorService(session);
        executorService.execute(() -> doWsReceive(session, message));
    }

    public void wsReceive(Session session, byte[] payload, int offset, int len) {
        ExecutorService executorService = selectedExecutorService(session);
        executorService.execute(() -> doWsReceive(session, payload, offset, len));
    }

    private void doWsReceive(Session session, String message) {
        try {
            EzyWsHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(session);
            if (handlerGroup != null) {
                handlerGroup.fireBytesReceived(message);
            }
        } catch (Throwable e) {
            logger.info(
                "I/O error at ws-message-received (session: {})",
                session,
                e
            );
        }
    }

    private void doWsReceive(Session session, byte[] payload, int offset, int len) {
        try {
            EzyWsHandlerGroup handlerGroup = handlerGroupManager.getHandlerGroup(session);
            if (handlerGroup != null) {
                handlerGroup.fireBytesReceived(payload, offset, len);
            }
        } catch (Throwable e) {
            logger.info(
                "I/O error at ws-message-received (session: {})",
                session,
                e
            );
        }
    }

    public void wsCloseConnection(Session session) {
        EzyWsHandlerGroup handlerGroup =
            handlerGroupManager.getHandlerGroup(session);
        if (handlerGroup != null) {
            handlerGroup.enqueueDisconnection();
        }
    }

    @Override
    public void destroy() {
        for (ExecutorService executorService : executorServices) {
            executorService.shutdownNow();
        }
    }

    private ExecutorService selectedExecutorService(Object channel) {
        int index = Math.abs(channel.hashCode() % threadPoolSize);
        return executorServices[index];
    }

    protected int getMaxBufferSize() {
        return EzyCoreConstants.MAX_READ_BUFFER_SIZE;
    }

    public static class Builder implements EzyBuilder<EzySocketDataReceiver> {
        protected int threadPoolSize;
        protected EzyHandlerGroupManager handlerGroupManager;

        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public Builder handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
            this.handlerGroupManager = handlerGroupManager;
            return this;
        }

        @Override
        public EzySocketDataReceiver build() {
            return new EzySocketDataReceiver(this);
        }
    }
}
