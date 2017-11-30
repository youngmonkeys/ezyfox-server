package com.tvd12.ezyfoxserver.nio.builder.impl;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Server;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.socket.EzyNioSocketWriter;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsWritingLoopHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyWebSocketServerBootstrap 
		implements EzyStartable, EzyDestroyable {

	private Server server;
	private SSLContext sslContext;
	private EzySocketEventLoopHandler writingLoopHandler;
	
	private EzyServerContext serverContext;
	private EzyHandlerGroupManager handlerGroupManager;
	private EzySessionTicketsQueue sessionTicketsQueue;
	
	public EzyWebSocketServerBootstrap(Builder builder) {
		this.sslContext = builder.sslContext;
		this.serverContext = builder.serverContext;
		this.handlerGroupManager = builder.handlerGroupManager;
		this.sessionTicketsQueue = builder.sessionTicketsQueue;
	}
	
	@Override
	public void start() throws Exception {
		server = newSocketServer();
		server.start();
		writingLoopHandler = newWritingLoopHandler();
		writingLoopHandler.start();
	}
	
	@Override
	public void destroy() {
		processWithLogException(writingLoopHandler::destroy);
		processWithLogException(server::stop);
	}
	
	private Server newSocketServer() {
		return newSocketServerCreator()
				.settings(getWsSettings())
				.sessionManager(getSessionManager())
				.sessionSettings(getSessionSettings())
				.handlerGroupManager(handlerGroupManager)
				.create();
	}
	
	private EzySocketEventLoopHandler newWritingLoopHandler() {
		EzySocketEventLoopHandler loopHandler = new EzyWsWritingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
		EzyNioSocketWriter eventHandler = new EzyNioSocketWriter();
		eventHandler.setHandlerGroupManager(handlerGroupManager);
		eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzyWebSocketServerCreator newSocketServerCreator() {
		if(isSslActive())
			return new EzyWebSocketSecureServerCreator(sslContext);
		return new EzyWebSocketServerCreator();
	}
	
	private int getSocketWriterPoolSize() {
		return 8;
	}
	
	private boolean isSslActive() {
		return getWsSettings().isSslActive();
	}
	
	private EzyWebSocketSetting getWsSettings() {
		return getServerSettings().getWebsocket();
	}
	
	private EzySessionManagementSetting getSessionSettings() {
		return getServerSettings().getSessionManagement();
	}
	
	private EzySettings getServerSettings() {
		return serverContext.getServer().getSettings();
	}
	
	private EzyManagers getServerManagers() {
		return serverContext.getServer().getManagers();
	}
	
	private EzyNioSessionManager getSessionManager() {
		return (EzyNioSessionManager) 
				getServerManagers().getManager(EzySessionManager.class);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyWebSocketServerBootstrap> {

		private SSLContext sslContext;
		private EzyServerContext serverContext;
		private EzyHandlerGroupManager handlerGroupManager;
		private EzySessionTicketsQueue sessionTicketsQueue;
		
		public Builder sslContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}
		
		public Builder serverContext(EzyServerContext context) {
			this.serverContext = context;
			return this;
		}
		
		public Builder handlerGroupManager(EzyHandlerGroupManager manager) {
			this.handlerGroupManager = manager;
			return this;
		}
		
		public Builder sessionTicketsQueue(EzySessionTicketsQueue queue) {
			this.sessionTicketsQueue = queue;
			return this;
		}
		
		@Override
		public EzyWebSocketServerBootstrap build() {
			return new EzyWebSocketServerBootstrap(this);
		}
	}
	
}
