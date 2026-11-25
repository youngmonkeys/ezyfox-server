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
import com.tvd12.ezyfoxserver.ssl.EzySslHandshakeSigner;

import java.util.Arrays;

public class EzyHandshakeController
    extends EzyAbstractServerController
    implements EzyServerController<EzyHandShakeRequest> {

    private EzySslHandshakeSigner sslHandshakeSigner;

    @SuppressWarnings("AbbreviationAsWordInName")
    @Override
    public void handle(EzyServerContext ctx, EzyHandShakeRequest request) {
        EzySession session = request.getSession();
        EzyHandshakeParams params = request.getParams();
        EzyHandshakeEvent event = newHandshakeEvent(session, params);
        ctx.handleEvent(EzyEventType.CLIENT_HANDSHAKE, event);
        handleSocketSSL(ctx, event);
        updateSession(session, event);
        updateSessionCompression(ctx, session, event);
        EzyResponse response = newHandShakeResponse(session, event);
        ctx.send(response, session, false);
        event.release();
    }

    @SuppressWarnings("AbbreviationAsWordInName")
    protected void handleSocketSSL(EzyServerContext ctx, EzyHandshakeEvent event) {
        EzySession session = event.getSession();
        if (!EzyConnectionType
            .SOCKET
            .getName()
            .equals(session.getConnectionType())
        ) {
            return;
        }
        boolean customizationSslEnable = ctx
            .getServer()
            .getSettings()
            .getSocket()
            .isCustomizationSslActive();
        if (!customizationSslEnable) {
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
        signHandshake(ctx, session, clientKey, sessionKey, encryptedSessionKey);
    }

    protected void signHandshake(
        EzyServerContext ctx,
        EzySession session,
        byte[] clientKey,
        byte[] sessionKey,
        byte[] encryptedSessionKey
    ) {
        if (clientKey == null
            || clientKey.length == 0
            || encryptedSessionKey == null
            || Arrays.equals(encryptedSessionKey, sessionKey)
        ) {
            return;
        }
        if (sslHandshakeSigner == null) {
            sslHandshakeSigner = ctx.getProperty(EzySslHandshakeSigner.class);
        }
        if (sslHandshakeSigner == null || !sslHandshakeSigner.isActive()) {
            return;
        }
        try {
            session.setPublicKey(
                sslHandshakeSigner.sign(clientKey, encryptedSessionKey)
            );
        } catch (Exception e) {
            logger.info("cannot sign handshake for session: {}", session, e);
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

    protected void updateSessionCompression(
        EzyServerContext ctx,
        EzySession session,
        EzyHandshakeEvent event
    ) {
        boolean compressionEnable = event.isEnableCompression()
            && EzyConnectionType
                .SOCKET
                .getName()
                .equals(session.getConnectionType())
            && ctx
                .getServer()
                .getSettings()
                .getSocket()
                .isCompressionEnable();
        ((EzyAbstractSession) session).setCompressionEnable(compressionEnable);
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
            params.isEnableEncryption(),
            params.isEnableCompression()
        );
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
        params.setCompressionEnable(session.isCompressionEnable());
        return new EzyHandShakeResponse(params);
    }
}
