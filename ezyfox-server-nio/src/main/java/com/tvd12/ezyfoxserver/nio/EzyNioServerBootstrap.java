package com.tvd12.ezyfoxserver.nio;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import javax.net.ssl.SSLContext;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApiAware;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApiAware;
import com.tvd12.ezyfoxserver.nio.constant.EzyNioThreadPoolSizes;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySocketSetting;
import com.tvd12.ezyfoxserver.setting.EzyStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionHandlingLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketExtensionRequestHandlingLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamHandlingLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketSystemRequestHandlingLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketUserRemovalHandlingLoopHandler;

import lombok.Setter;


public class EzyNioServerBootstrap extends EzyHttpServerBootstrap {

	private EzySocketServerBootstrap socketServerBootstrap;
	private EzyWebSocketServerBootstrap websocketServerBootstrap;
	
	@Setter
	private SSLContext sslContext;
	@Setter
	private EzyResponseApi responseApi;
	@Setter
	private EzyStreamingApi streamingApi;
	@Setter
	private EzySocketRequestQueues requestQueues;
	@Setter
	private EzySocketStreamQueue streamQueue;
	@Setter
	private EzyHandlerGroupManager handlerGroupManager;
	@Setter
	private EzySessionTicketsQueue socketSessionTicketsQueue;
	@Setter
	private EzySessionTicketsQueue websocketSessionTicketsQueue;
	@Setter
	private EzySocketDisconnectionQueue socketDisconnectionQueue;
	
	private EzySocketEventLoopOneHandler systemRequestHandlingLoopHandler;
	
	private EzySocketEventLoopOneHandler extensionRequestHandlingLoopHandler;
	
	private EzySocketEventLoopOneHandler streamHandlingLoopHandler;
	
	private EzySocketEventLoopOneHandler socketDisconnectionHandlingLoopHandler;
	
	private EzySocketEventLoopOneHandler socketUserRemovalHandlingLoopHandler;
	
	@Override
	protected void setupServer() {
		EzyServer server = getServer();
		((EzyResponseApiAware)server).setResponseApi(responseApi);
		((EzyStreamingApiAware)server).setStreamingApi(streamingApi);
	}
	
	@Override
	protected void startOtherBootstraps(Runnable callback) throws Exception {
		startSocketServerBootstrap();
		startWebSocketServerBootstrap();
		startRequestHandlingLoopHandlers();
		startStreamHandlingLoopHandlers();
		startDisconnectionHandlingLoopHandlers();
		startUserRemovalHandlingLoopHandlers();
		callback.run();
	}
	
	private void startSocketServerBootstrap() throws Exception {
		EzySocketSetting socketSetting = getSocketSetting();
		if(!socketSetting.isActive()) return;
		logger.debug("starting tcp socket server bootstrap ....");
		socketServerBootstrap = newSocketServerBootstrap();
		socketServerBootstrap.start();
		logger.debug("tcp socket server bootstrap has started");
	}
	
	protected void startWebSocketServerBootstrap() throws Exception {
		EzyWebSocketSetting socketSetting = getWebSocketSetting();
		if(!socketSetting.isActive()) return;
		logger.debug("starting websocket server bootstrap ....");
		websocketServerBootstrap = newWebSocketServerBootstrap();
		websocketServerBootstrap.start();
		logger.debug("websockt server bootstrap has started");
	}
	
	private void startRequestHandlingLoopHandlers() throws Exception {
		systemRequestHandlingLoopHandler = newSystemRequestHandlingLoopHandler();
		extensionRequestHandlingLoopHandler = newExtensionRequestHandlingLoopHandler();
		systemRequestHandlingLoopHandler.start();
		extensionRequestHandlingLoopHandler.start();
	}
	
	private void startStreamHandlingLoopHandlers() throws Exception {
		EzySettings settings = this.getServerSettings();
		EzyStreamingSetting streamingSetting = settings.getStreaming();
		if(streamingSetting.isEnable()) {
			streamHandlingLoopHandler = newSocketStreamHandlingLoopHandler();
			streamHandlingLoopHandler.start();
		}
	}
	
	private void startDisconnectionHandlingLoopHandlers() throws Exception {
		socketDisconnectionHandlingLoopHandler = newSocketDisconnectionHandlingLoopHandler();
		socketDisconnectionHandlingLoopHandler.start();
	}
	
	private void startUserRemovalHandlingLoopHandlers() throws Exception {
		socketUserRemovalHandlingLoopHandler = newSocketUserRemovalHandlingLoopHandler();
		socketUserRemovalHandlingLoopHandler.start();
	}
	
	private EzySocketServerBootstrap newSocketServerBootstrap() {
		return EzySocketServerBootstrap.builder()
				.serverContext(context)
				.handlerGroupManager(handlerGroupManager)
				.sessionTicketsQueue(socketSessionTicketsQueue)
				.build();
	}
	
	private EzyWebSocketServerBootstrap newWebSocketServerBootstrap() {
		return EzyWebSocketServerBootstrap.builder()
				.serverContext(context)
				.sslContext(sslContext)
				.handlerGroupManager(handlerGroupManager)
				.sessionTicketsQueue(websocketSessionTicketsQueue)
				.build();
	}
	
	private EzySocketEventLoopOneHandler newSystemRequestHandlingLoopHandler() {
		EzySocketEventLoopOneHandler loopHandler = new EzySocketSystemRequestHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getSystemRequestHandlerPoolSize());
		EzySocketRequestHandler eventHandler = new EzySocketSystemRequestHandler();
		eventHandler.setRequestQueue(requestQueues.getSystemQueue());
		eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopOneHandler newExtensionRequestHandlingLoopHandler() {
		EzySocketEventLoopOneHandler loopHandler = new EzySocketExtensionRequestHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getExtensionRequestHandlerPoolSize());
		EzySocketRequestHandler eventHandler = new EzySocketExtensionRequestHandler();
		eventHandler.setRequestQueue(requestQueues.getExtensionQueue());
		eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopOneHandler newSocketStreamHandlingLoopHandler() {
		EzySocketEventLoopOneHandler loopHandler = new EzySocketStreamHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getStreamHandlerPoolSize());
		EzySocketStreamHandler eventHandler = new EzySocketStreamHandler();
		eventHandler.setStreamQueue(streamQueue);
		eventHandler.setDataHandlerGroupFetcher(handlerGroupManager);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopOneHandler newSocketDisconnectionHandlingLoopHandler() {
		EzySocketEventLoopOneHandler loopHandler = new EzySocketDisconnectionHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketDisconnectionHandlerPoolSize());
		EzySocketDisconnectionHandler eventHandler = new EzySocketDisconnectionHandler();
		eventHandler.setDataHandlerGroupRemover(handlerGroupManager);
		eventHandler.setDisconnectionQueue(socketDisconnectionQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopOneHandler newSocketUserRemovalHandlingLoopHandler() {
		EzySocketEventLoopOneHandler loopHandler = new EzySocketUserRemovalHandlingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketUserRemovalHandlerPoolSize());
		EzySocketUserRemovalHandler eventHandler = new EzySocketUserRemovalHandler();
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private int getStreamHandlerPoolSize() {
		return EzyNioThreadPoolSizes.STREAM_HANDLER;
	}
	
	private int getSystemRequestHandlerPoolSize() {
		return EzyNioThreadPoolSizes.SYSTEM_REQUEST_HANDLER;
	}
	
	private int getExtensionRequestHandlerPoolSize() {
		return EzyNioThreadPoolSizes.EXTENSION_REQUEST_HANDLER;
	}
	
	private int getSocketDisconnectionHandlerPoolSize() {
		return EzyNioThreadPoolSizes.SOCKET_DISCONNECTION_HANDLER;
	}
	
	private int getSocketUserRemovalHandlerPoolSize() {
		return EzyNioThreadPoolSizes.SOCKET_USER_REMOVAL_HANDLER;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(socketServerBootstrap != null)
			processWithLogException(() -> socketServerBootstrap.destroy());
		if(websocketServerBootstrap != null)
			processWithLogException(() -> websocketServerBootstrap.destroy());
		if(handlerGroupManager != null)
			processWithLogException(() -> handlerGroupManager.destroy());
		if(systemRequestHandlingLoopHandler != null)
			processWithLogException(() -> systemRequestHandlingLoopHandler.destroy());
		if(extensionRequestHandlingLoopHandler != null)
			processWithLogException(() -> extensionRequestHandlingLoopHandler.destroy());
		if(socketDisconnectionHandlingLoopHandler != null)
			processWithLogException(() -> socketDisconnectionHandlingLoopHandler.destroy());
		if(socketUserRemovalHandlingLoopHandler != null)
			processWithLogException(() -> socketUserRemovalHandlingLoopHandler.destroy());
	}
	
}
