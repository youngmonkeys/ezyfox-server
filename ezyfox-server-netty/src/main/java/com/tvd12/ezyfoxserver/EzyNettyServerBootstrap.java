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
	
	@Override
	protected void startOtherBootstraps() throws Exception {
		startServerBootstrap();
		startWsServerBootstrap();
		waitAndShutdownEventLoopGroups();
	}
	
	protected void startServerBootstrap() throws Exception {
		startBootstrap(serverBootstrap, "socket");
	}
	
	protected void startWsServerBootstrap() throws Exception {
		startBootstrap(wsServerBootstrap, "websocket");
	}
	
	private void startBootstrap(ServerBootstrap bootstrap, String type) throws Exception {
		getLogger().debug("starting {} server bootstrap ....", type);
		ChannelFuture future = serverBootstrap.bind().sync();
		getLogger().debug("{} server bootstrap started", type);
		waitAndCloseChannelFuture(future, type);
	}
	
	@SuppressWarnings("unchecked")
	protected void waitAndCloseChannelFuture(ChannelFuture future, String type) {
		future.channel().closeFuture()
			.addListener(newChannelFutureListener(type)).syncUninterruptibly();
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
