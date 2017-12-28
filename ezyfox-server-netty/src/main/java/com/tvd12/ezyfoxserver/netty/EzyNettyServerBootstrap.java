package com.tvd12.ezyfoxserver.netty;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrap;
import com.tvd12.ezyfoxserver.netty.constant.EzyNettyThreadPoolSizes;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandlingLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandlingLoopHandler;

import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Setter;

public class EzyNettyServerBootstrap extends EzyHttpServerBootstrap {

	@Setter
	protected EventLoopGroup childGroup;
	@Setter
	protected EventLoopGroup parentGroup;
	@Setter
	private EzySocketRequestQueues requestQueues;
	@Setter
	private EzyHandlerGroupManager handlerGroupManager;
	@Setter
	protected EzySocketServerBootstrap socketServerBootstrap;
	@Setter
	protected EzySocketServerBootstrap websocketServerBootstrap;
	
	private EzySocketEventLoopHandler systemRequestHandlingLoopHandler;
	
	private EzySocketEventLoopHandler extensionRequestHandlingLoopHandler;
	
	@Override
	protected void startOtherBootstraps(Runnable callback) throws Exception {
		startServerBootstrap();
		startWsServerBootstrap();
		startRequestHandlingLoopHandlers();
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
	
	private void startRequestHandlingLoopHandlers() throws Exception {
		systemRequestHandlingLoopHandler = newSystemRequestHandlingLoopHandler();
		extensionRequestHandlingLoopHandler = newExtensionRequestHandlingLoopHandler();
		systemRequestHandlingLoopHandler.start();
		extensionRequestHandlingLoopHandler.start();
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
	
	private EzySocketEventLoopHandler newSystemRequestHandlingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzySocketSystemRequestHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getSystemRequestHandlerPoolSize());
		EzySocketRequestHandler eventHandler = new EzySocketSystemRequestHandler();
		eventHandler.setRequestQueue(requestQueues.getSystemQueue());
		eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newExtensionRequestHandlingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzySocketExtensionRequestHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getExtensionRequestHandlerPoolSize());
		EzySocketRequestHandler eventHandler = new EzySocketExtensionRequestHandler();
		eventHandler.setRequestQueue(requestQueues.getExtensionQueue());
		eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private int getSystemRequestHandlerPoolSize() {
		return EzyNettyThreadPoolSizes.SYSTEM_REQUEST_HANDLER;
	}
	
	private int getExtensionRequestHandlerPoolSize() {
		return EzyNettyThreadPoolSizes.EXTENSION_REQUEST_HANDLER;
	}
	
}
