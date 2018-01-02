package com.tvd12.ezyfoxserver.netty.handler;

import static com.tvd12.ezyfoxserver.socket.EzySocketRequestBuilder.socketRequestBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPacketsAware;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDataSender;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDataSenderAware;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketChannelDelegate;
import com.tvd12.ezyfoxserver.socket.EzySocketDataDecoderGroupAware;
import com.tvd12.ezyfoxserver.socket.EzySocketRequest;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySessionStats;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyAbstractHandlerGroup 
		extends EzyLoggable 
		implements 
			EzyHandlerGroup, 
			EzyImmediateDataSender, 
			EzySocketChannelDelegate,
			EzyDroppedPackets,
			EzyDestroyable {

	protected final EzyChannel channel;
	protected final EzyNettyDataHandler handler;
	
	protected final AtomicInteger sessionCount;
	protected final EzySessionStats sessionStats;
	protected final EzyNetworkStats networkStats;
	
	protected final ExecutorService statsThreadPool;
	
	protected final EzySocketRequestQueues requestQueues;
	protected final AtomicReference<EzyNettySession> session;
	protected final EzySocketChannelDelegate channelDelegate;
	protected final EzySessionTicketsQueue sessionTicketsQueue;
	
	protected EzyAbstractHandlerGroup(Builder builder) {
		this.session = new AtomicReference<EzyNettySession>();
		this.channel = builder.channel;
		this.sessionCount = builder.sessionCount;
		this.sessionStats = builder.sessionStats;
		this.networkStats = builder.networkStats;
		this.statsThreadPool = builder.statsThreadPool;
		this.requestQueues = builder.requestQueues;
		this.channelDelegate = builder.channelDelegate;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
		this.handler = newDataHandler(builder.serverContext);
	}
	
	private EzyNettyDataHandler newDataHandler(EzyServerContext context) {
		EzySimpleNettyDataHandler handler = new EzySimpleNettyDataHandler();
		handler.setChannel(channel);
		handler.setContext(context);
		handler.setChannelDelegate(this);
		return handler;
	}
	
	@Override
	public void fireChannelInactive() {
		fireChannelInactive(EzyDisconnectReason.UNKNOWN);
	}
	
	@Override
	public void fireChannelInactive(EzyConstant reason) {
		try {
			handler.channelInactive(reason);
		}
		catch(Exception e) {
			getLogger().error("handler inactive error", e);
		}
		finally {
			sessionStats.setCurrentSessions(sessionCount.decrementAndGet());
		}
	}
	
	@Override
	public void fireExceptionCaught(Throwable throwable) {
		try {
			handler.exceptionCaught(throwable);
		}
		catch(Exception e) {
			fireChannelInactive(EzyDisconnectReason.SERVER_ERROR);
		}
	}
	
	@Override
	public void firePacketSend(EzyPacket packet) throws Exception {
		try {
			channel.write(packet.getData());
		}
		finally {
			packet.release();
		}
	}
	
	@Override
	public void fireChannelRead(EzyArray msg) throws Exception {
		EzySocketRequest request = socketRequestBuilder()
				.data((EzyArray) msg)
				.session(getSession())
				.build();
		boolean success = requestQueues.add(request);
		if(!success)
			getLogger().info("request queue is full, drop incomming request");
	}
	
	@Override
	public void fireChannelRead(EzyCommand cmd, EzyArray msg) throws Exception {
		handler.channelRead(cmd, msg);
	}
	
	@Override
	public Object fireDecodeData(Object data) throws Exception {
		return data;
	}
	
	@Override
	public EzyNettySession fireChannelActive() throws Exception {
		EzyNettySession ss = handler.channelActive();
		ss.setSessionTicketsQueue(sessionTicketsQueue);
		((EzyDroppedPacketsAware)ss).setDroppedPackets(this);
		((EzyImmediateDataSenderAware)ss).setImmediateDataSender(this);
		((EzySocketDataDecoderGroupAware)ss).setDataDecoderGroup(this);
		session.set(ss);
		sessionStats.addSessions(1);
		sessionStats.setCurrentSessions(sessionCount.incrementAndGet());
		return ss;
	}
	
	@Override
	public void sendPacketNow(EzyPacket packet) {
		try {
			channel.write(packet.getData());
		} catch (Exception e) {
			getLogger().warn("write packet {} to channel {} error", packet, channel);
		}
		finally {
			packet.release();
		}
	}
	
	@Override
	public void addDroppedPacket(EzyPacket packet) {
		networkStats.addDroppedOutPackets(1);
	}
	
	@Override
	public final void onChannelInactivated(EzyChannel channel) {
		channelDelegate.onChannelInactivated(channel);
	}
	
	@Override
	public void fireBytesSent(int bytes) {
		statsThreadPool.execute(() -> addWrittenBytes(bytes));
	}
	
	@Override
	public void fireBytesReceived(int bytes) {
		statsThreadPool.execute(() -> addReadBytes(bytes));
	}
	
	private synchronized void addReadBytes(int count) {
		networkStats.addReadBytes(count);
		networkStats.addReadPackets(1);
		if(isSessionConnected())
			getSession().addReadBytes(count);
	}
	
	private synchronized void addWrittenBytes(int count) {
		networkStats.addWrittenBytes(count);
		networkStats.addWrittenPackets(1);
		if(isSessionConnected())
			getSession().addWrittenBytes(count);
	}
	
	protected final EzyNettySession getSession() {
		return session.get();
	}
	
	protected final boolean isSessionConnected() {
		return getSession() != null;
	}
	
	@Override
	public void destroy() {
		session.set(null);
	}
	
	public abstract static class Builder implements EzyBuilder<EzyAbstractHandlerGroup> {
		
		protected EzyChannel channel;
		
		protected AtomicInteger sessionCount;
		protected EzySessionStats sessionStats;
		protected EzyNetworkStats networkStats;
		
		protected ExecutorService statsThreadPool;
		
		protected EzyServerContext serverContext;
		protected EzySocketRequestQueues requestQueues;
		protected EzySocketChannelDelegate channelDelegate;
		protected EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder channel(EzyChannel channel) {
			this.channel = channel;
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
