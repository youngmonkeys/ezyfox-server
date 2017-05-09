package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleLoginRequest;

public class EzyLoginControllerTest extends EzyBaseControllerTest {

    @Test
    public void test() {
        EzyServerContext ctx = newServerContext();
        EzySession session = newSession();
        session.setReconnectToken("abcdef");
        EzyArray data = newLoginData();
        EzyLoginController controller = new EzyLoginController();
        EzyLoginRequest request = EzySimpleLoginRequest.builder()
                .params(mapDataToRequestParams(data))
                .session(session)
                .build();
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
            protected void process(EzyServerContext ctx, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzyLoginRequest request = EzySimpleLoginRequest.builder()
                .params(mapDataToRequestParams(data))
                .session(session)
                .build();
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
            protected void firePluginEvent(EzyServerContext ctx, EzyUserLoginEvent event) {
                throw new EzyLoginErrorException();
            }
        };
        EzyLoginRequest request = EzySimpleLoginRequest.builder()
                .params(mapDataToRequestParams(data))
                .session(session)
                .build();
        controller.handle(ctx, request);
    }
    
    private EzyArray newLoginData() {
        return newArrayBuilder()
                .append("dungtv")
                .append("123456")
                .append(newArrayBuilder()
                        .append("123456"))
                .build();
    }
    
    private EzyArray newLoginData1() {
        return newArrayBuilder()
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
