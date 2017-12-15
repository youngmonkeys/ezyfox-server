package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrap;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Setter;

public class EzyNettyServerBootstrap extends EzyHttpServerBootstrap {

	@Setter
	protected EventLoopGroup childGroup;
	@Setter
	protected EventLoopGroup parentGroup;
	@Setter
	protected EzySocketServerBootstrap socketServerBootstrap;
	@Setter
	protected EzySocketServerBootstrap websocketServerBootstrap;
	
	@Override
	protected void startOtherBootstraps(Runnable callback) throws Exception {
		startServerBootstrap();
		startWsServerBootstrap();
		callback.run();
		waitAndCloseChannelFuture();
		waitAndCloseWsChannelFuture();
		waitAndShutdownEventLoopGroups();
	}
	
	protected void startServerBootstrap() throws Exception {
		EzySocketSetting setting = getSocketSetting();
		if(setting.isActive())
			socketServerBootstrap.start();
	}
	
	protected void startWsServerBootstrap() throws Exception {
		EzyWebSocketSetting setting = getWebSocketSetting();
		if(setting.isActive())
			websocketServerBootstrap.start();
	}
	
	protected void waitAndCloseChannelFuture() {
		if(socketServerBootstrap != null)
			socketServerBootstrap.waitAndCloseChannelFuture();
	}
	
	protected void waitAndCloseWsChannelFuture() {
		if(websocketServerBootstrap != null)
			websocketServerBootstrap.waitAndCloseChannelFuture();
	}
	
	@SuppressWarnings("unchecked")
	protected void waitAndShutdownEventLoopGroup(EventLoopGroup group, String type) {
		group.shutdownGracefully()
			.addListener(newEventLoopGroupFutureListener(type)).syncUninterruptibly();
	}
	
	@SuppressWarnings("rawtypes")
	protected GenericFutureListener newEventLoopGroupFutureListener(String type) {
		return future -> getLogger().info("{} event loop group shutdown", type);
	}
	
	protected void waitAndShutdownEventLoopGroups() {
		waitAndShutdownEventLoopGroup(childGroup, "child");
		waitAndShutdownEventLoopGroup(parentGroup, "parent");
	}
	
}
