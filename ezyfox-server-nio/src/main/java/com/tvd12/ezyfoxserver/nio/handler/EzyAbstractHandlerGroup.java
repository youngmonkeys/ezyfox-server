package com.tvd12.ezyfoxserver.nio.handler;

import static com.tvd12.ezyfoxserver.socket.EzySocketRequestBuilder.socketRequestBuilder;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.nio.delegate.EzySocketChannelDelegate;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyImmediateDataSender;
import com.tvd12.ezyfoxserver.socket.EzyImmediateDataSenderAware;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketDataDecoderGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDataDecoderGroupAware;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyAbstractHandlerGroup
		<
			D extends EzyDestroyable,
			E extends EzyNioDataEncoder
		>
		extends EzyLoggable
		implements EzyImmediateDataSender, EzySocketDataDecoderGroup, EzySocketChannelDelegate, EzyDestroyable {

	protected final EzyChannel channel;
	
	protected D decoder;
	protected E encoder;
	protected final EzyNioDataHandler handler;

	protected final AtomicInteger sessionCount;
	protected final EzySessionStats sessionStats;
	protected final EzyNetworkStats networkStats;
	
	protected final ExecutorService statsThreadPool;
	protected final ExecutorService codecThreadPool;
	protected final ExecutorService bytesWriterThreadPool;

	protected final EzySocketRequestQueues requestQueues;
	protected final AtomicReference<EzyNioSession> session;
	protected final EzySocketChannelDelegate channelDelegate;
	protected final EzySessionTicketsQueue sessionTicketsQueue;
	
	public EzyAbstractHandlerGroup(Builder builder) {
		this.session = new AtomicReference<EzyNioSession>();
		this.channel = builder.channel;
		this.sessionCount = builder.sessionCount;
		this.sessionStats = builder.sessionStats;
		this.networkStats = builder.networkStats;
		this.statsThreadPool = builder.statsThreadPool;
		this.codecThreadPool = builder.codecThreadPool;
		this.bytesWriterThreadPool = builder.bytesWriterThreadPool;
		this.requestQueues = builder.requestQueues;
		this.channelDelegate = builder.channelDelegate;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
		
		this.decoder = newDecoder(builder.decoder);
		this.encoder = newEncoder(builder.encoder);
		this.handler = newDataHandler(builder.serverContext);
	}
	
	protected abstract D newDecoder(Object decoder);
	
	protected abstract E newEncoder(Object encoder);
	
	private EzyNioDataHandler newDataHandler(EzyServerContext context) {
		EzySimpleNioDataHandler handler = new EzySimpleNioDataHandler(channel);
		handler.setContext(context);
		handler.setChannelDelegate(this);
		return handler;
	}
	
	public final void fireChannelInactive() {
		fireChannelInactive(EzyDisconnectReason.UNKNOWN);
	}
	
	public final void fireChannelInactive(EzyConstant reason) {
		try {
			decoder.destroy();
			handler.channelInactive(reason);
		}
		catch(Exception e) {
			getLogger().error("handler inactive error", e);
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
	
	public final void firePacketSend(EzyPacket packet) throws Exception {
		executeSendingPacket(packet);
	}
	
	public final void fireChannelRead(EzyCommand cmd, EzyArray msg) throws Exception {
		handler.channelRead(cmd, msg);
	}
	
	public final EzyNioSession fireChannelActive() throws Exception {
		EzyNioSession ss = handler.channelActive();
		ss.setSessionTicketsQueue(sessionTicketsQueue);
		((EzyImmediateDataSenderAware)ss).setImmediateDataSender(this);
		((EzySocketDataDecoderGroupAware)ss).setDataDecoderGroup(this);
		session.set(ss);
		sessionStats.addSessions(1);
		sessionStats.setCurrentSessions(sessionCount.incrementAndGet());
		return ss;
	}
	
	@Override
	public void sendPacketNow(EzyPacket packet) {
		executeSendingPacket(packet);
	}
	
	@Override
	public final void onChannelInactivated(EzyChannel channel) {
		channelDelegate.onChannelInactivated(channel);
	}
	
	protected final void handleReceivedData(Object data) {
		EzySocketRequest request = socketRequestBuilder()
			.data((EzyArray) data)
			.session(getSession())
			.build();
		boolean success = requestQueues.add(request);
		if(!success)
			getLogger().info("request queue is full, drop incomming request");
	}
	
	private void executeSendingPacket(EzyPacket packet) {
		if(isSessionConnected()) {
			sendPacketToClient0(packet);
		}
	}
	
	public Object fireDecodeData(Object data) throws Exception {
		return encodeData(data);
	}
	
	protected abstract Object encodeData(Object data) throws Exception;
	
	private void sendPacketToClient0(EzyPacket packet) {
		try {
			EzyChannel channel = getSession().getChannel();
			if(canWriteBytes(channel)) {
				int writeBytes = writePacketToSocket(packet);
				executeAddWrittenBytes(writeBytes);
			}
		}
		catch(Exception e) {
			Object bytes = packet.getData();
			networkStats.addWriteErrorPackets(1);
			networkStats.addWriteErrorBytes(getWriteBytesSize(bytes));
			getLogger().warn("can't send bytes: " + bytes + " to session: " + getSession() + ", error: " + e.getMessage());
		}
	}
	
	protected int writePacketToSocket(EzyPacket packet) throws Exception {
		Object bytes = packet.getData();
		int writeBytes = channel.write(bytes);
		packet.release();
		return writeBytes;
	}
	
	protected long getWriteBytesSize(Object bytes) {
		if(bytes instanceof byte[])
			return ((byte[])bytes).length;
		return ((ByteBuffer)bytes).remaining();
	}
	
	private boolean canWriteBytes(EzyChannel channel) {
		return channel != null && channel.isConnected();
	}
	
	protected final void executeAddReadBytes(int bytes) {
		statsThreadPool.execute(() -> addReadBytes(bytes));
	}
	
	private void executeAddWrittenBytes(int bytes) {
		statsThreadPool.execute(() -> addWrittenBytes(bytes));
	}
	
	private synchronized void addReadBytes(int count) {
		getSession().addReadBytes(count);
		networkStats.addReadBytes(count);
		networkStats.addReadPackets(1);
	}
	
	private synchronized void addWrittenBytes(int count) {
		getSession().addWrittenBytes(count);
		networkStats.addWrittenBytes(count);
		networkStats.addWrittenPackets(1);
	}
	
	protected final EzyNioSession getSession() {
		return session.get();
	}
	
	protected final boolean isSessionConnected() {
		return getSession() != null;
	}
	
	@Override
	public void destroy() {
		session.set(null);
	}
	
	public static abstract class Builder implements EzyBuilder<EzyHandlerGroup> {

		protected EzyChannel channel;

		protected AtomicInteger sessionCount;
		protected EzySessionStats sessionStats;
		protected EzyNetworkStats networkStats;
		
		protected ExecutorService statsThreadPool;
		protected ExecutorService codecThreadPool;
		protected ExecutorService bytesWriterThreadPool;
		
		protected Object decoder;
		protected EzyNioObjectToByteEncoder encoder;
		protected EzyServerContext serverContext;
		protected EzySocketRequestQueues requestQueues;
		protected EzySocketChannelDelegate channelDelegate;
		protected EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder channel(EzyChannel channel) {
			this.channel = channel;
			return this;
		}
		
		public Builder decoder(Object decoder) {
			this.decoder = decoder;
			return this;
		}
		
		public Builder encoder(Object encoder) {
			this.encoder = (EzyNioObjectToByteEncoder) encoder;
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
		
		public Builder bytesWriterThreadPool(ExecutorService bytesWriterThreadPool) {
			this.bytesWriterThreadPool = bytesWriterThreadPool;
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
		
		public Builder channelDelegate(EzySocketChannelDelegate delegate) {
			this.channelDelegate = delegate;
			return this;
		}
		
		public Builder sessionTicketsQueue(EzySessionTicketsQueue queue) {
			this.sessionTicketsQueue = queue;
			return this;
		}
		
	}
	
}
