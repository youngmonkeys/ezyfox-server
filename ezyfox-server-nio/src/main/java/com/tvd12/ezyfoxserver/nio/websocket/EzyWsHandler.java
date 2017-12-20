package com.tvd12.ezyfoxserver.nio.websocket;

import java.net.SocketTimeoutException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@WebSocket
public class EzyWsHandler extends EzyLoggable {

	private final EzyNioSessionManager sessionManager;
	private final EzySessionManagementSetting settings;
	private final EzyHandlerGroupManager handlerGroupManager;

	public EzyWsHandler(Builder builder) {
		this.settings = builder.settings;
		this.sessionManager = builder.sessionManager;
		this.handlerGroupManager = builder.handlerGroupManager;
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		getLogger().debug("close session: {}, statusCode = {}, reason = {}", session.getRemoteAddress(), statusCode, reason);
		setChannelClosed(session);
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		dataHandler.fireChannelInactive();
	}

	@OnWebSocketError
	public void onError(Session session, Throwable throwable) {
		getLogger().debug("error: session: " + session.getRemoteAddress(), throwable);
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		if (throwable instanceof SocketTimeoutException) {
			dataHandler.fireChannelInactive(EzyDisconnectReason.IDLE);
		} else {
			dataHandler.fireExceptionCaught(throwable);
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		session.setIdleTimeout(settings.getSessionMaxIdleTime());
		EzyChannel channel = new EzyWsChannel(session);
		EzyWsHandlerGroup dataHandler = handlerGroupManager.newHandlerGroup(channel, EzyConnectionType.WEBSOCKET);
		dataHandler.fireChannelActive();
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws Exception {
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		dataHandler.fireBytesReceived(message);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, byte[] payload, int offset, int len) throws Exception {
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		dataHandler.fireBytesReceived(payload, offset, len);
	}

	private void setChannelClosed(Session connection) {
		EzyNioSession session = sessionManager.getSession(connection);
		EzyWsChannel channel = (EzyWsChannel) session.getChannel();
		channel.setClosed();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements EzyBuilder<EzyWsHandler> {

		private EzyNioSessionManager sessionManager;
		private EzySessionManagementSetting settings;
		private EzyHandlerGroupManager handlerGroupManager;

		public Builder settings(EzySessionManagementSetting settings) {
			this.settings = settings;
			return this;
		}

		public Builder sessionManager(EzyNioSessionManager sessionManager) {
			this.sessionManager = sessionManager;
			return this;
		}

		public Builder handlerGroupManager(EzyHandlerGroupManager handlerGroupManager) {
			this.handlerGroupManager = handlerGroupManager;
			return this;
		}

		@Override
		public EzyWsHandler build() {
			return new EzyWsHandler(this);
		}
	}
}
