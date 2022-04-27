package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.socket.EzySocketDataReceiver;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.Set;
import java.util.concurrent.TimeoutException;

import static com.tvd12.ezyfoxserver.nio.websocket.EzyWsCloseStatus.CLOSE_BY_SERVER;

@WebSocket
public class EzyWsHandler extends EzyLoggable {

    private final EzySocketDataReceiver socketDataReceiver;
    private final EzyNioSessionManager sessionManager;
    private final EzyHandlerGroupManager handlerGroupManager;
    private final EzySessionManagementSetting sessionManagementSetting;

    private static final Set<Integer> IGNORE_STATUS_CODES = Sets.newHashSet(
        CLOSE_BY_SERVER.getCode()
    );

    public EzyWsHandler(Builder builder) {
        this.socketDataReceiver = builder.socketDataReceiver;
        this.sessionManager = builder.sessionManager;
        this.handlerGroupManager = builder.handlerGroupManager;
        this.sessionManagementSetting = builder.sessionManagementSetting;
    }

    public static Builder builder() {
        return new Builder();
    }

    private boolean isIgnoreStatusCode(int statusCode) {
        return IGNORE_STATUS_CODES.contains(statusCode);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        logger.debug(
            "close session: {}, statusCode = {}, reason = {}",
            session.getRemoteAddress(),
            statusCode,
            reason
        );
        if (isIgnoreStatusCode(statusCode)) {
            return;
        }
        setChannelClosed(session);
        socketDataReceiver.wsCloseConnection(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        EzyWsHandlerGroup dataHandler = handlerGroupManager.getHandlerGroup(session);
        if (dataHandler == null) {
            logger.debug(
                "error on session: {}, but data handler removed",
                session.getRemoteAddress(),
                throwable
            );
        }
        if (throwable instanceof TimeoutException) {
            logger.debug("session {}: Timeout on Read", session.getRemoteAddress());
        } else if (dataHandler != null) {
            logger.debug("error on session: {}", session.getRemoteAddress(), throwable);
            dataHandler.fireExceptionCaught(throwable);
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        long sessionMaxIdleTime = sessionManagementSetting.getSessionMaxIdleTime();
        session.setIdleTimeout(sessionMaxIdleTime);
        EzyChannel channel = new EzyWsChannel(session);
        handlerGroupManager.newHandlerGroup(channel, EzyConnectionType.WEBSOCKET);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        socketDataReceiver.wsReceive(session, message);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, byte[] payload, int offset, int len) {
        socketDataReceiver.wsReceive(session, payload, offset, len);
    }

    private void setChannelClosed(Session connection) {
        EzyNioSession session = sessionManager.getSession(connection);
        if (session != null) {
            EzyWsChannel channel = (EzyWsChannel) session.getChannel();
            if (channel != null) {
                channel.setClosed();
            }
        }
    }

    public static class Builder implements EzyBuilder<EzyWsHandler> {

        private EzySocketDataReceiver socketDataReceiver;
        private EzyNioSessionManager sessionManager;
        private EzyHandlerGroupManager handlerGroupManager;
        private EzySessionManagementSetting sessionManagementSetting;

        public Builder socketDataReceiver(EzySocketDataReceiver socketDataReceiver) {
            this.socketDataReceiver = socketDataReceiver;
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

        public Builder sessionManagementSetting(EzySessionManagementSetting sessionManagementSetting) {
            this.sessionManagementSetting = sessionManagementSetting;
            return this;
        }

        @Override
        public EzyWsHandler build() {
            return new EzyWsHandler(this);
        }
    }
}
