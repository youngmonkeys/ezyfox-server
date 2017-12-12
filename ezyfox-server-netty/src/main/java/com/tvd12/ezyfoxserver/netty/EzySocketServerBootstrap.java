package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Setter;

public class EzySocketServerBootstrap 
		extends EzyLoggable
		implements EzyStartable {

	@Setter
	protected ServerBootstrap bootstrap;
	@Setter
	protected EzySocketEventLoopHandler writingLoopHandler;
	
	protected ChannelFuture channelFuture;
	
	@Override
	public void start() throws Exception {
		startServerBootstrap();
		startWritingLoopHandler();
	}
	
	protected void startServerBootstrap() throws Exception {
		getLogger().info("starting {} server bootstrap ....", getSocketType());
		channelFuture = bootstrap.bind().sync();
		getLogger().info("{} server bootstrap has started", getSocketType());
	}
	
	protected void startWritingLoopHandler() throws Exception {
		writingLoopHandler.start();
	}
	
	@SuppressWarnings("unchecked")
	public void waitAndCloseChannelFuture() {
		channelFuture.channel().closeFuture()
			.addListener(newChannelFutureListener()).syncUninterruptibly();
	}
	
	@SuppressWarnings("rawtypes")
	protected GenericFutureListener newChannelFutureListener() {
		return future -> getLogger().info("{} channel future closed", getSocketType());
	}
	
	protected String getSocketType() {
		return "tcp socket";
	}
	
}
