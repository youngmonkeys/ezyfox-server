package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
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

	private int port;
	private String address;

	private EzyWebSocketSetting settings;
	private EzyHandlerGroupManager handlerGroupManager;
	private EzySessionManagementSetting sessionSettings;
	
	public EzyWebSocketServerCreator port(int port) {
		this.port = port;
		return this;
	}
	
	public EzyWebSocketServerCreator address(String address) {
		this.address = address;
		return this;
	}
	
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
		Server server = new Server(newSocketAddress());
		server.setHandler(contextHandler);
		return server;
	}
	
	private EzyWsHandler newWsHandler() {
		return EzyWsHandler.builder()
				.settings(sessionSettings)
				.handlerGroupManager(handlerGroupManager)
				.build();
	}
	
	private InetSocketAddress newSocketAddress() {
		return new InetSocketAddress(address, port);
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
