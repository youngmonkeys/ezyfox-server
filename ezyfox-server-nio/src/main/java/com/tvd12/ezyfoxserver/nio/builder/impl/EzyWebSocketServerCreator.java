package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;

public class EzyWebSocketServerCreator {

	protected EzySocketDataReceiver socketDataReceiver;
	protected EzyWebSocketSetting setting;
	protected EzyNioSessionManager sessionManager;
	protected EzyHandlerGroupManager handlerGroupManager;
	protected EzySessionManagementSetting sessionManagementSetting;
	
	public EzyWebSocketServerCreator socketDataReceiver(EzySocketDataReceiver socketDataReceiver) {
		this.socketDataReceiver = socketDataReceiver;
		return this;
	}
	
	public EzyWebSocketServerCreator setting(EzyWebSocketSetting setting) {
		this.setting = setting;
		return this;
	}
	
	public EzyWebSocketServerCreator sessionManager(EzyNioSessionManager sessionManager) {
		this.sessionManager = sessionManager;
		return this;
	}
	
	public EzyWebSocketServerCreator handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
		this.handlerGroupManager = handlerGroupManager;
		return this;
	}
	
	public EzyWebSocketServerCreator sessionManagementSetting(EzySessionManagementSetting sessionManagementSetting) {
		this.sessionManagementSetting = sessionManagementSetting;
		return this;
	}
	
	public Server create() {
		ContextHandler wsContextHandler = new ContextHandler("/ws");
		wsContextHandler.setAllowNullPathInfo(true);
		EzyWsHandler wsHandler = newWsHandler();
		wsContextHandler.setHandler(newWebSocketHandler(wsHandler));
		QueuedThreadPool threadPool = new QueuedThreadPool(16, 8);
		threadPool.setName("ezyfox-ws-handler");
		Server server = new Server(threadPool);
		ContextHandlerCollection contextHandlers = new ContextHandlerCollection();
		contextHandlers.addHandler(wsContextHandler);
		if(setting.isManagementEnable())
			contextHandlers.addHandler(managementHandler());
		server.setHandler(contextHandlers);
		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setSecureScheme("https");
		httpConfig.setSecurePort(setting.getSslPort());
		ServerConnector wsConnector = new ServerConnector(server);
		wsConnector.setPort(setting.getPort());
		wsConnector.setHost(setting.getAddress());
		server.addConnector(wsConnector);
		configServer(server, httpConfig, wsConnector);
		return server;
	}
	
	private ContextHandler managementHandler() {
		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/management");
		contextHandler.addServlet(HealthCheckServlet.class, "/health-check");
		return contextHandler;
	}
	
	protected void configServer(
			Server server, HttpConfiguration httpConfig, ServerConnector wsConnector) {
		
	}
	
	private EzyWsHandler newWsHandler() {
		return EzyWsHandler.builder()
				.sessionManager(sessionManager)
				.handlerGroupManager(handlerGroupManager)
				.sessionManagementSetting(sessionManagementSetting)
				.socketDataReceiver(socketDataReceiver)
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
			@Override
			public void configure(WebSocketServletFactory factory) {
				WebSocketPolicy policy = factory.getPolicy();
				factory.setCreator(newWebSocketCreator(handler));
				policy.setMaxTextMessageSize(setting.getMaxFrameSize());
				policy.setMaxBinaryMessageSize(setting.getMaxFrameSize());
				policy.setMaxTextMessageBufferSize(setting.getMaxFrameSize());
				policy.setMaxBinaryMessageBufferSize(setting.getMaxFrameSize());
			}
		};
	}
	
	public static class HealthCheckServlet extends HttpServlet {
		private static final long serialVersionUID = -5456527539188272097L;
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.setStatus(200);
		}
	}
	
}
