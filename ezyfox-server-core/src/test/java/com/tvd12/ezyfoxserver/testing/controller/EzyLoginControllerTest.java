package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.request.EzySimpleLoginRequest;

public class EzyLoginControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        session.setReconnectToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }
    
    @Test(expectedExceptions = {EzyLoginErrorException.class})
    public void test1() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        session.setReconnectToken("abcdef");
        EzyArray data = newLoginData1();
        EzyLoginController controller = new EzyLoginController() {
            @Override
            protected void process(EzyServerContext ctx, EzyZoneContext zoneContext, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }
    
    @Test(expectedExceptions = {EzyLoginErrorException.class})
    public void test2() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        session.setReconnectToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController() {
            @Override
            protected void firePluginEvent(EzyZoneContext zoneContext, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }
    
    private EzyArray newLoginData() {
        return newArrayBuilder()
                .append("example")
                .append("dungtv")
                .append("123456")
                .append(newArrayBuilder()
                        .append("123456"))
                .build();
    }
    
    private EzyArray newLoginData1() {
        return newArrayBuilder()
                .append("example")
                .append("dungtv")
                .append("123456")
                .append(newArrayBuilder())
                .build();
    }
    
    @Override
    protected EzyConstant getCommand() {
        return EzyCommand.LOGIN;
    }
    
}
