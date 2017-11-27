package com.tvd12.ezyfoxserver.nio.builder.impl;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

public class EzyWebSocketServerCreator {

	protected EzyWebSocketSetting settings;
	protected EzyHandlerGroupManager handlerGroupManager;
	protected EzySessionManagementSetting sessionSettings;
	
	public EzyWebSocketServerCreator settings(EzyWebSocketSetting settings) {
		this.settings = settings;
		return this;
	}
	
	public EzyWebSocketServerCreator sessionSettings(EzySessionManagementSetting sessionSettings) {
		this.sessionSettings = sessionSettings;
		return this;
	}
	
	public EzyWebSocketServerCreator handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
		this.handlerGroupManager = handlerGroupManager;
		return this;
	}
	
	public Server create() {
		ContextHandler contextHandler = new ContextHandler("/ws");
		contextHandler.setAllowNullPathInfo(true);
		EzyWsHandler wsHandler = newWsHandler();
		contextHandler.setHandler(newWebSocketHandler(wsHandler));
		Server server = new Server();
		server.setHandler(contextHandler);
		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setSecureScheme("https");
		httpConfig.setSecurePort(settings.getSslPort());
		ServerConnector wsConnector = new ServerConnector(server);
		wsConnector.setPort(settings.getPort());
		wsConnector.setHost(settings.getAddress());
		server.addConnector(wsConnector);
		configServer(server, httpConfig, wsConnector);
		return server;
	}
	
	protected void configServer(
			Server server, HttpConfiguration httpConfig, ServerConnector wsConnector) {
		
	}
	
	private EzyWsHandler newWsHandler() {
		return EzyWsHandler.builder()
				.settings(sessionSettings)
				.handlerGroupManager(handlerGroupManager)
				.build();
	}
	
	private WebSocketCreator newWebSocketCreator(EzyWsHandler handler) {
		return new WebSocketCreator() {
			@Override
			public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
				return handler;
			}
		};
	}
	
	private WebSocketHandler newWebSocketHandler(EzyWsHandler handler) {
		return new WebSocketHandler() {
			public void configure(WebSocketServletFactory factory) {
				WebSocketPolicy policy = factory.getPolicy();
				factory.setCreator(newWebSocketCreator(handler));
				policy.setMaxTextMessageSize(settings.getMaxFrameSize());
				policy.setMaxBinaryMessageSize(settings.getMaxFrameSize());
				policy.setMaxTextMessageBufferSize(settings.getMaxFrameSize());
				policy.setMaxBinaryMessageBufferSize(settings.getMaxFrameSize());
			}
		};
	}
	
}
