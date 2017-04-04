package com.tvd12.ezyfoxserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Setter;

public class EzyNettyServerBootstrap extends EzyServerBootstrap {

	@Setter
	protected EventLoopGroup childGroup;
	@Setter
	protected EventLoopGroup parentGroup;
	@Setter
	protected ServerBootstrap serverBootstrap;
	@Setter
	protected ServerBootstrap wsServerBootstrap;
	
	protected ChannelFuture channelFuture;
	protected ChannelFuture wsChannelFuture;
	
	@Override
	protected void startOtherBootstraps() throws Exception {
		startServerBootstrap();
		startWsServerBootstrap();
		waitAndCloseChannelFuture();
		waitAndCloseWsChannelFuture();
		waitAndShutdownEventLoopGroups();
	}
	
	protected void startServerBootstrap() throws Exception {
		getLogger().debug("starting server bootstrap ....");
		channelFuture = serverBootstrap.bind().sync();
		getLogger().debug("server bootstrap started");
	}
	
	protected void startWsServerBootstrap() throws Exception {
		getLogger().debug("starting server bootstrap ....");
		wsChannelFuture = wsServerBootstrap.bind().sync();
		getLogger().debug("server bootstrap started");
	}
	
	@SuppressWarnings("unchecked")
	protected void waitAndCloseChannelFuture() {
		channelFuture.channel().closeFuture()
			.addListener(newChannelFutureListener("socket")).syncUninterruptibly();
	}
	
	@SuppressWarnings("unchecked")
	protected void waitAndCloseWsChannelFuture() {
		wsChannelFuture.channel().closeFuture()
			.addListener(newChannelFutureListener("websocket")).syncUninterruptibly();
	}
	
	@SuppressWarnings("rawtypes")
	protected GenericFutureListener newChannelFutureListener(String type) {
		return (future) -> getLogger().info("{} channel future closed", type);
	}
	
	@SuppressWarnings("unchecked")
	protected void waitAndShutdownEventLoopGroup(EventLoopGroup group, String type) {
		group.shutdownGracefully()
			.addListener(newEventLoopGroupFutureListener(type)).syncUninterruptibly();
	}
	
	@SuppressWarnings("rawtypes")
	protected GenericFutureListener newEventLoopGroupFutureListener(String type) {
		return (future) -> getLogger().info("{} event loop group shutdown", type);
	}
	
	protected void waitAndShutdownEventLoopGroups() {
		waitAndShutdownEventLoopGroup(childGroup, "child");
		waitAndShutdownEventLoopGroup(parentGroup, "parent");
	}
	
}
