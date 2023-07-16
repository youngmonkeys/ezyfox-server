package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.security.EzyAesCrypt;
import com.tvd12.ezyfox.security.EzyAsyCrypt;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyHandshakeEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleHandshakeEvent;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.EzyHandshakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.response.EzyHandShakeResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;

public class EzyHandshakeController
    extends EzyAbstractServerController
    implements EzyServerController<EzyHandShakeRequest> {

    @SuppressWarnings("AbbreviationAsWordInName")
    @Override
    public void handle(EzyServerContext ctx, EzyHandShakeRequest request) {
        EzySession session = request.getSession();
        EzyHandshakeParams params = request.getParams();
        EzyHandshakeEvent event = newHandshakeEvent(session, params);
        ctx.handleEvent(EzyEventType.CLIENT_HANDSHAKE, event);
        handleSocketSSL(ctx, event);
        updateSession(session, event);
        EzyResponse response = newHandShakeResponse(session, event);
        ctx.send(response, session, false);
        event.release();
    }

    @SuppressWarnings("AbbreviationAsWordInName")
    protected void handleSocketSSL(EzyServerContext ctx, EzyHandshakeEvent event) {
        EzySession session = event.getSession();
        if (session.getConnectionType() == EzyConnectionType.WEBSOCKET) {
            return;
        }
        boolean enableCustomizationSsl = ctx
            .getServer()
            .getSettings()
            .getSocket()
            .isCustomizationSslActive();
        if (!enableCustomizationSsl) {
            return;
        }
        if (!event.isEnableEncryption()) {
            return;
        }
        byte[] clientKey = event.getClientKey();
        byte[] sessionKey = event.getSessionKey();
        if (sessionKey == null) {
            sessionKey = EzyAesCrypt.randomKey();
            event.setSessionKey(sessionKey);
        }
        byte[] encryptedSessionKey = event.getEncryptedSessionKey();
        if (encryptedSessionKey == null) {
            encryptedSessionKey = sessionKey;
            try {
                if (clientKey != null && clientKey.length > 0) {
                    encryptedSessionKey = EzyAsyCrypt.builder()
                        .publicKey(clientKey)
                        .build()
                        .encrypt(sessionKey);
                }
            } catch (Exception e) {
                logger.debug("cannot encrypt session key for session: {}", session, e);
            }
            event.setEncryptedSessionKey(encryptedSessionKey);
        }
    }

    protected void updateSession(EzySession session, EzyHandshakeEvent event) {
        session.setClientId(event.getClientId());
        session.setClientKey(event.getClientKey());
        session.setClientType(event.getClientType());
        session.setClientVersion(event.getClientVersion());
        session.setSessionKey(event.getSessionKey());
        ((EzyAbstractSession) session).setBeforeToken(event.getReconnectToken());
    }

    protected EzyHandshakeEvent newHandshakeEvent(
        EzySession session,
        EzyHandshakeParams params
    ) {
        return new EzySimpleHandshakeEvent(
            session,
            params.getClientId(),
            params.getClientKey(),
            params.getClientType(),
            params.getClientVersion(),
            params.getReconnectToken(),
            params.isEnableEncryption());
    }

    protected EzyResponse newHandShakeResponse(
        EzySession session,
        EzyHandshakeEvent event
    ) {
        EzyHandShakeParams params = new EzyHandShakeParams();
        params.setServerPublicKey(session.getPublicKey());
        params.setReconnectToken(session.getToken());
        params.setSessionId(session.getId());
        params.setSessionKey(event.getEncryptedSessionKey());
        return new EzyHandShakeResponse(params);
    }
}
