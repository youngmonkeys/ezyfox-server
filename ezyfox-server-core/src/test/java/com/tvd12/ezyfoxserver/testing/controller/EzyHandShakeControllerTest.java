package com.tvd12.ezyfoxserver.testing.controller;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleHandShakeRequest;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;

public class EzyHandShakeControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        EzyArray data = newHandShakeData();
        EzyHandShakeController controller = new EzyHandShakeController();
        EzyHandShakeRequest request = EzySimpleHandShakeRequest.builder()
                .params(mapDataToRequestParams(data))
                .session(session)
                .build();
        controller.handle(ctx, request);
    }
    
    private EzyArray newHandShakeData() {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
                .append("adroid#1")
                .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
                .append("reconnectToken#1")
                .append("android")
                .append("1.0.0")
                .build();
    }
    
    @Override
    protected EzySession newSession() {
        KeyPair keyPair = newRSAKeys();
        EzySession session = super.newSession();
        session.setReconnectToken("reconnectToken#1");
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }
    
}
