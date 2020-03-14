package com.tvd12.ezyfoxserver.nio.handler;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPacketsAware;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliverAware;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;

public abstract class EzyAbstractHandlerGroup
		<
			D extends EzyDestroyable
		>
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
	protected final ExecutorService codecThreadPool;

	protected final EzyNioSession session;
	protected final EzySocketRequestQueues requestQueues;
	protected final EzySocketStreamQueue streamQueue;
	protected final EzySessionTicketsQueue sessionTicketsQueue;
	protected final EzySocketDisconnectionQueue disconnectionQueue;
	
	public EzyAbstractHandlerGroup(Builder builder) {
		this.session = builder.session;
		this.sessionCount = builder.sessionCount;
		this.sessionStats = builder.sessionStats;
		this.networkStats = builder.networkStats;
		this.statsThreadPool = builder.statsThreadPool;
		this.codecThreadPool = builder.codecThreadPool;
		this.requestQueues = builder.requestQueues;
		this.streamQueue = builder.streamQueue;
		this.streamingEnable = builder.isStreamingEnable();
		this.disconnectionQueue = builder.disconnectionQueue;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;

		this.channel = session.getChannel();
		
		this.decoder = newDecoder(builder.decoder);
		this.handler = newDataHandler(builder);

		session.setDisconnectionQueue(disconnectionQueue);
		session.setSessionTicketsQueue(sessionTicketsQueue);
		((EzyDroppedPacketsAware)session).setDroppedPackets(this);
		((EzyImmediateDeliverAware)session).setImmediateDeliver(this);
		sessionStats.addSessions(1);
		sessionStats.setCurrentSessions(sessionCount.incrementAndGet());
	}
	
	protected abstract D newDecoder(Object decoder);
	
	private EzyNioDataHandler newDataHandler(Builder builder) {
		EzySimpleNioDataHandler handler = new EzySimpleNioDataHandler(
				builder.serverContext,
				builder.session);
		return handler;
	}
	
	public final void enqueueDisconnection(EzyConstant reason) {
		if(session != null)
			session.disconnect(reason);
	}
	
	public final void fireChannelInactive() {
		fireChannelInactive(EzyDisconnectReason.UNKNOWN);
	}
	
	public final void fireChannelInactive(EzyConstant reason) {
		try {
			handler.channelInactive(reason);
		}
		catch(Exception e) {
			logger.error("handler inactive error", e);
		}
		finally {
			sessionStats.setCurrentSessions(sessionCount.decrementAndGet());
		}
	}
	
	public final void fireExceptionCaught(Throwable throwable) {
		try {
			handler.exceptionCaught(throwable);
		}
		catch(Exception e) {
			fireChannelInactive(EzyDisconnectReason.SERVER_ERROR);
		}
	}
	
	public final void firePacketSend(EzyPacket packet, Object writeBuffer) throws Exception {
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
			sendPacketNow0(packet);
		}
		finally {
			packet.release();
		}
	}
	
	protected void sendPacketNow0(EzyPacket packet) {
		executeSendingPacket(packet, null);
	}
	
	@Override
	public void addDroppedPacket(EzyPacket packet) {
		networkStats.addDroppedOutPackets(1);
		networkStats.addDroppedOutBytes(packet.getSize());
	}
	
	protected final void handleReceivedData(Object data, int dataSize) {
		EzySocketRequest request = newSocketRequest(data);
		boolean success = requestQueues.add(request);
		if(!success) {
			networkStats.addDroppedInPackets(1);
			networkStats.addDroppedInBytes(dataSize);
			logger.info("request queue is full, drop incomming request");
		}
	}
	
	private EzySocketRequest newSocketRequest(Object data) {
		return new EzySimpleSocketRequest(session, (EzyArray) data);
	}
	
	protected final void executeSendingPacket(EzyPacket packet, Object writeBuffer) {
		if(isSessionConnected()) {
			sendPacketToClient0(packet, writeBuffer);
		}
	}
	
	private void sendPacketToClient0(EzyPacket packet, Object writeBuffer) {
		try {
			EzyChannel channel = session.getChannel();
			if(canWriteBytes(channel)) {
				int writeBytes = 0;
				if(packet.getTransportType() == EzyTransportType.TCP)
					writeBytes = writePacketToSocket(packet, writeBuffer);
				else
					writeBytes = writeUdpPacketToSocket(packet, writeBuffer);
				executeAddWrittenBytes(writeBytes);
			}
		}
		catch(Exception e) {
			int packetSize = packet.getSize();
			networkStats.addWriteErrorPackets(1);
			networkStats.addWriteErrorBytes(packetSize);
			logger.warn("can't send {} bytes to session: {}, error: {}({})", packetSize, session, e.getClass().getName(), e.getMessage());
		}
	}
	
	protected int writePacketToSocket(EzyPacket packet, Object writeBuffer) throws Exception {
		try {
			Object bytes = packet.getData();
			int writeBytes = channel.write(bytes, packet.isBinary());
			packet.release();
			return writeBytes;
		}
		finally {
			packet.release();
		}
	}
	
	protected int writeUdpPacketToSocket(EzyPacket packet, Object writeBuffer) throws Exception {
		try {
			EzyDatagramChannelPool udpChannelPool = session.getDatagramChannelPool();
			if(udpChannelPool == null)
				return 0;
			SocketAddress clientAddress = session.getUdpClientAddress();
			if(clientAddress == null)
				return 0;
			byte[] bytes = getBytesToWrite(packet);
			int bytesToWrite = bytes.length;
			ByteBuffer buffer = getWriteBuffer((ByteBuffer)writeBuffer, bytesToWrite);
			buffer.clear();
			buffer.put(bytes);
			buffer.flip();
			DatagramChannel channel = udpChannelPool.getChannel();
			int writtenByes = channel.send(buffer, clientAddress);
			return writtenByes;
		}
		finally {
			packet.release();
		}
	}
	
	protected byte[] getBytesToWrite(EzyPacket packet) {
		return (byte[])packet.getData();
	}
	
	protected ByteBuffer getWriteBuffer(ByteBuffer fixed, int bytesToWrite) {
		return bytesToWrite > fixed.capacity() ? ByteBuffer.allocate(bytesToWrite) : fixed;
	}
	
	private boolean canWriteBytes(EzyChannel channel) {
		if(channel == null)
			return false;
		if(channel.isConnected())
			return true;
		return false;
	}
	
	protected final void executeAddReadBytes(int bytes) {
		statsThreadPool.execute(() -> addReadBytes(bytes));
	}
	
	private void executeAddWrittenBytes(int bytes) {
		statsThreadPool.execute(() -> addWrittenBytes(bytes));
	}
	
	private void addReadBytes(int count) {
		session.addReadBytes(count);
		networkStats.addReadBytes(count);
		networkStats.addReadPackets(1);
	}
	
	private void addWrittenBytes(int count) {
		session.addWrittenBytes(count);
		networkStats.addWrittenBytes(count);
		networkStats.addWrittenPackets(1);
	}
	
	protected final boolean isSessionConnected() {
		return session != null;
	}
	
	public EzyNioSession getSession() {
		return session;
	}
	
	@Override
	public void destroy() {
		processWithLogException(() -> decoder.destroy());
		processWithLogException(() -> handler.destroy());
	}
	
	public static abstract class Builder implements EzyBuilder<EzyHandlerGroup> {

		protected AtomicInteger sessionCount;
		protected EzySessionStats sessionStats;
		protected EzyNetworkStats networkStats;
		
		protected ExecutorService statsThreadPool;
		protected ExecutorService codecThreadPool;
		
		protected Object decoder;
		protected EzyNioSession session;
		protected EzyServerContext serverContext;
		protected EzySocketRequestQueues requestQueues;
		protected EzySocketStreamQueue streamQueue;
		protected EzySessionTicketsQueue sessionTicketsQueue;
		protected EzySocketDisconnectionQueue disconnectionQueue;
		
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
		
		public Builder codecThreadPool(ExecutorService codecThreadPool) {
			this.codecThreadPool = codecThreadPool;
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
		
		public Builder requestQueues(EzySocketRequestQueues requestQueues) {
			this.requestQueues = requestQueues;
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
		
		protected boolean isStreamingEnable() {
			return serverContext.getServer().getSettings().getStreaming().isEnable();
		}
	}
	
}
