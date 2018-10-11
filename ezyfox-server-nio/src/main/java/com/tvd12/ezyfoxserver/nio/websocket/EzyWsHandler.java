package com.tvd12.ezyfoxserver.nio.websocket;

import static com.tvd12.ezyfoxserver.nio.websocket.EzyWsCloseStatus.CLOSE_BY_SERVER;

import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Sets;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

@WebSocket
public class EzyWsHandler extends EzyLoggable {

	private final EzyNioSessionManager sessionManager;
	private final EzySessionManagementSetting settings;
	private final EzyHandlerGroupManager handlerGroupManager;
	
	private static final Set<Integer> IGNORE_STATUS_CODES = Sets.newHashSet(
			CLOSE_BY_SERVER.getCode()
	);

	public EzyWsHandler(Builder builder) {
		this.settings = builder.settings;
		this.sessionManager = builder.sessionManager;
		this.handlerGroupManager = builder.handlerGroupManager;
	}
	
	private boolean isIgnoreStatusCode(int statusCode) {
		return IGNORE_STATUS_CODES.contains(statusCode);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		getLogger().debug("close session: {}, statusCode = {}, reason = {}", session.getRemoteAddress(), statusCode, reason);
		if(isIgnoreStatusCode(statusCode)) 
			return;
		setChannelClosed(session);
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		dataHandler.enqueueDisconnection();
	}
	
	@OnWebSocketError
	public void onError(Session session, Throwable throwable) {
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		if(dataHandler == null) {
			getLogger().debug("error on session: " + session.getRemoteAddress() + ", but data handler removed, details: " + throwable);
		}
		if (throwable instanceof TimeoutException) {
			getLogger().debug("session " + session.getRemoteAddress() + ": Timeout on Read");
		}
		else if(dataHandler != null) {
			getLogger().debug("error on session: " + session.getRemoteAddress() + ", details: " + throwable);
			dataHandler.fireExceptionCaught(throwable);
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		session.setIdleTimeout(settings.getSessionMaxIdleTime());
		EzyChannel channel = new EzyWsChannel(session);
		handlerGroupManager.newHandlerGroup(channel, EzyConnectionType.WEBSOCKET);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws Exception {
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		if(dataHandler == null)
			getLogger().debug("session: " + session.getRemoteAddress() + " disconnected, data handler = null");
		else
			dataHandler.fireBytesReceived(message);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, byte[] payload, int offset, int len) throws Exception {
		EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
		if(dataHandler == null)
			getLogger().debug("session: " + session.getRemoteAddress() + " disconnected, data handler = null");
		else
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
