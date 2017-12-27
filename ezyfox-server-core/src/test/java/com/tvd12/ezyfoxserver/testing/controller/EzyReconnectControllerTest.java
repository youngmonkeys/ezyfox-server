package com.tvd12.ezyfoxserver.testing.controller;

import java.security.KeyPair;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyReconnectController;
import com.tvd12.ezyfoxserver.delegate.EzyAbstractSessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyReconnectRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleReconnectParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleReconnectRequest;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.testing.MyTestUser;

public class EzyReconnectControllerTest extends EzyBaseControllerTest {

    private EzyServerContext context;
    
    public EzyReconnectControllerTest() {
        super();
        context = newServerContext();
    }
    
    @Test
    public void test1() {
        EzyAbstractSession oldSession = getSessionManager(context).provideSession(EzyConnectionType.SOCKET);
        oldSession.setDelegate(new EzyAbstractSessionDelegate() {
        });
        EzySession session = newSession();
        EzyReconnectRequest request = EzySimpleReconnectRequest.builder()
                .params(EzySimpleReconnectParams.builder().build())
                .session(session)
                .oldSession(oldSession)
                .build();
        EzyReconnectController controller = new EzyReconnectController();
        controller.handle(context, request);
    }
    
    @Test
    public void test2() {
        MyTestUser user = new MyTestUser();
        user.setName("dungtv");
        EzyAbstractSession oldSession = getSessionManager(context).provideSession(EzyConnectionType.SOCKET);
        oldSession.setDelegate(new EzyAbstractSessionDelegate() {
        });
        user.addSession(oldSession);
        getUserManager(context).addUser(oldSession, user);
        EzySession session = newSession();
        EzyReconnectController controller = new EzyReconnectController();
        session.setReconnectToken("abcdef");
        EzyReconnectRequest request = EzySimpleReconnectRequest.builder()
                .params(EzySimpleReconnectParams.builder().build())
                .session(session)
                .oldSession(oldSession)
                .build();
        controller.handle(context, request);
    }

    @SuppressWarnings("unused")
    private EzyArray newHandShakeData(String token) {
        KeyPair keyPair = newRSAKeys();
        return newArrayBuilder()
                .append("adroid#1")
                .append(EzyBase64.encode2utf(keyPair.getPublic().getEncoded()))
                .append(token)
                .append("android")
                .append("1.0.0")
                .build();
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.HANDSHAKE;
    }
}
