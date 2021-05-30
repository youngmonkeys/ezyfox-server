package com.tvd12.ezyfoxserver.testing.controller;

import static org.testng.Assert.assertEquals;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandshakeController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandshakeParams;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;

public class EzyHandShakeController2Test extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession first = getSessionManager(ctx).provideSession(EzyConnectionType.SOCKET);
        System.err.println("first.token:    " + first);
        System.err.println("alive sessions: " + getSessionManager(ctx).getAliveSessions());
        assertEquals(getSessionManager(ctx).containsSession(first.getId()), true);
        EzySession session = getSessionManager(ctx).provideSession(EzyConnectionType.SOCKET);
        System.err.println("session: " + session);
        EzyArray data = newHandShakeData(first.getToken());
        EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
        request.deserializeParams(data);
        request.setSession(session);
        EzyHandshakeParams requestParams = request.getParams();
        assertEquals(first.getToken(), requestParams.getReconnectToken());
        EzyHandshakeController controller = new EzyHandshakeController();
        controller.handle(ctx, request);
    }
    
    private EzyArray newHandShakeData(String reconnectToken) {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
                .append("adroid#1")
                .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
                .append("android")
                .append("1.0.0")
                .append(true)
                .append(reconnectToken)
                .build();
    }
    
    protected EzySession newSession(String reconnectToken) {
        KeyPair keyPair = newRSAKeys();
        EzySession session = super.newSession();
        session.setToken(reconnectToken);
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }
    
}
