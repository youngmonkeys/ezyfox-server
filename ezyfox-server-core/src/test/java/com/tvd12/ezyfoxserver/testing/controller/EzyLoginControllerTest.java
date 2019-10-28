package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.request.EzySimpleLoginRequest;
import static org.mockito.Mockito.*;

public class EzyLoginControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController();
        EzySimpleLoginRequest request = new EzySimpleLoginRequest();
        request.deserializeParams(data);
        request.setSession(session);
        controller.handle(ctx, request);
    }
    
    @Test(expectedExceptions = {EzyLoginErrorException.class})
    public void test1() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
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
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
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
    
    @Test
    public void allowGuestLoginTest() {
        EzySimpleServerContext ctx = (EzySimpleServerContext) newServerContext();
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        EzySession session = newSession();
        session.setToken("abcdef");
        EzyArray data = newLoginData();
        data.set(1, "");
        EzyLoginController controller = new EzyLoginController();
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
