package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.context.EzyContexts;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.netty.builder.impl.EzyServerBootstrapCreator;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettySocketWriter;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettySocketWritingLoopHandler;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.setting.EzyBaseSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;

public class EzySocketServerBootstrap 
		extends EzyLoggable
		implements EzyStartable {

	protected EventLoopGroup childGroup;
	protected EventLoopGroup parentGroup;
	protected ServerBootstrap bootstrap;
	protected ChannelFuture channelFuture;
	protected EzyServerContext serverContext;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	protected EzySocketEventLoopHandler writingLoopHandler;
	
	protected EzySocketServerBootstrap(Builder<?> builder) {
		this.childGroup = builder.childGroup;
		this.parentGroup = builder.parentGroup;
		this.serverContext = builder.serverContext;
	}
	
	@Override
	public void start() throws Exception {
		prestart();
		startServerBootstrap();
		startWritingLoopHandler();
	}
	
	private void prestart() {
		this.sessionTicketsQueue = newSessionTicketsQueue();
	}
	
	private void startServerBootstrap() throws Exception {
		getLogger().info("starting {} server bootstrap ....", getSocketType());
		bootstrap = createNettyServerBootstrap();
		channelFuture = bootstrap.bind().sync();
		getLogger().info("{} server bootstrap has started", getSocketType());
	}
	
	private void startWritingLoopHandler() throws Exception {
		writingLoopHandler = createSocketWritingLoopHandler();
		writingLoopHandler.start();
	}
	
	@SuppressWarnings("unchecked")
	public void waitAndCloseChannelFuture() {
		channelFuture.channel().closeFuture()
			.addListener(newChannelFutureListener()).syncUninterruptibly();
	}
	
	@SuppressWarnings("rawtypes")
	private GenericFutureListener newChannelFutureListener() {
		return future -> getLogger().info("{} channel future closed", getSocketType());
	}
	
	protected String getSocketType() {
		return "tcp socket";
	}
	
	protected EzySessionTicketsQueue newSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	private EzySocketEventLoopHandler createSocketWritingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = newSocketWritingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
		EzyNettySocketWriter eventHandler = newSocketWriter();
		eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	protected EzyNettySocketWriter newSocketWriter() {
		return new EzyNettySocketWriter();
	}
	
	protected EzySocketEventLoopHandler newSocketWritingLoopHandler() {
		return new EzyNettySocketWritingLoopHandler();
	}
	
	protected EzyServerBootstrapCreator<?> newServerBootstrapCreator() {
		EzySocketSetting setting = getSocketSetting();
		return EzyServerBootstrapCreator.newInstance()
				.port(getSocketPort())
				.maxRequestSize(setting.getMaxRequestSize());
	}
	
	private ServerBootstrap createNettyServerBootstrap() {
		EzyServerBootstrapCreator<?> creator = newServerBootstrapCreator();
		return creator
				.address(getSocketAddress())
				.context(serverContext)
				.childGroup(childGroup)
				.parentGroup(parentGroup)
				.codecCreator(newCodecCreator())
				.sessionTicketsQueue(sessionTicketsQueue)
				.create();
	}
	
	private int getSocketPort() {
		return getCommonSocketSetting().getPort();
	}
	
	private String getSocketAddress() {
		return getCommonSocketSetting().getAddress();
	}
	
	protected int getSocketWriterPoolSize() {
		return 8;
	}
	
	protected final EzyCodecCreator newCodecCreator() {
        return EzyClasses.newInstance(getCodecCreatorClassName());
    }
	
	protected EzySettings getSettings() {
		return EzyContexts.getSettings(serverContext);
	}
	
	private EzySocketSetting getSocketSetting() {
        return getSettings().getSocket();
    }
	
	protected EzyBaseSocketSetting getCommonSocketSetting() {
		return getSocketSetting();
	}
	
	private String getCodecCreatorClassName() {
        return getCommonSocketSetting().getCodecCreator();
    }
	
	public static Builder<?> builder() {
		return new Builder<>();
	}
	
	@SuppressWarnings({"unchecked"})
	public static class Builder<B extends Builder<B>> 
			implements EzyBuilder<EzySocketServerBootstrap> {
		
		protected EventLoopGroup childGroup;
		protected EventLoopGroup parentGroup;
		protected EzyServerContext serverContext;
		
		public B childGroup(EventLoopGroup childGroup) {
			this.childGroup = childGroup;
			return (B)this;
		}
		
		public B parentGroup(EventLoopGroup parentGroup) {
			this.parentGroup = parentGroup;
			return (B)this;
		}
		
		public B serverContext(EzyServerContext serverContext) {
			this.serverContext = serverContext;
			return (B)this;
		}
		
		@Override
		public EzySocketServerBootstrap build() {
			return new EzySocketServerBootstrap(this);
		}
		
	}
	
}
