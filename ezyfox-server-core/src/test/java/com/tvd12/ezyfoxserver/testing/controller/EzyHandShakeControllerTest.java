package com.tvd12.ezyfoxserver.testing.controller;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyHandshakeController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;
import com.tvd12.test.performance.Performance;

public class EzyHandShakeControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        EzyArray data = newHandShakeData();
        EzyHandshakeController controller = new EzyHandshakeController();
        EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }
    
    @Test
    public void testDeserializeParamsPerformance() {
        EzyArray data = newHandShakeData();
        long time = Performance.create()
                .test(() -> {
                    EzySimpleHandshakeRequest request = new EzySimpleHandshakeRequest();
                    request.deserializeParams(data);
                })
                .getTime();
        System.out.println("testDeserializeParamsPerformance, time = " + time);
                
    }
    
    private EzyArray newHandShakeData() {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
                .append("adroid#1")
                .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
                .append("android")
                .append("1.0.0")
                .append(true)
                .append("reconnectToken#1")
                .build();
    }
    
    @Override
    protected EzySession newSession() {
        KeyPair keyPair = newRSAKeys();
        EzySession session = super.newSession();
        session.setToken("reconnectToken#1");
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }
    
}
