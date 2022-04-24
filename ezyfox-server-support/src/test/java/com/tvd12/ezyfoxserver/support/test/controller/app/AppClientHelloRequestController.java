package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;
import com.tvd12.ezyfoxserver.support.test.exception.RequestException;

@EzyRequestController
public class AppClientHelloRequestController {

    @EzyDoHandle("c_hello")
    public void handleHello(EzyAppContext context, EzyUserSessionEvent event, Hello data) {
        System.out.println("app: c_hello: " + data.getWho());
    }

    @EzyDoHandle("c_hello1")
    public void handleHello1(EzyAppContext context, EzyUserSessionEvent event) {
        System.out.println("app: c_hello1");
    }

    @EzyDoHandle("c_hello2")
    public void handleHello2() {
        System.out.println("app: c_hello2");
    }

    @EzyDoHandle("c_hello3")
    public void handleHello3(EzyUser user) {
        System.out.println("app: c_hello3");
    }

    @EzyDoHandle("c_hello4")
    public void handleHello4(EzySession session) {
        System.out.println("app: c_hello4");
    }

    @EzyDoHandle("c_hello5")
    public void handleHello5(EzyContext context) {
        System.out.println("app: c_hello5");
    }

    @EzyDoHandle("c_hello6")
    public void handleHello6(EzyContext context, String value, Exception e) {
        System.out.println("app: c_hello6");
    }

    @EzyDoHandle("requestException")
    public void handleRequestException(EzyContext context, Hello data) {
        throw new RequestException(getClass().getSimpleName() + ":handlerequestException");
    }

    @EzyDoHandle("requestException2")
    public void handleRequestException2(
            EzyContext context,
            String cmd,
            @EzyRequestData Hello data) throws Exception {
        throw new Exception(getClass().getSimpleName() + ":handleRequestException2, cmd = " + cmd);
    }

    @EzyDoHandle("requestException3")
    public void handleRequestException3(EzyContext context) throws Exception {
        throw new IllegalArgumentException(getClass().getSimpleName() + ":handleRequestException3");
    }

}
