package com.tvd12.ezyfoxserver.support.test.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;
import com.tvd12.ezyfoxserver.support.test.data.GreetResponse;

@EzyRequestController("Big")
public class HelloController {

    @EzyAutoBind
    protected EzyResponseFactory appResponseFactory;

    @EzyDoHandle("Hello")
    public void greet(GreetRequest request, EzyUser user, EzySession session) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }

    @EzyDoHandle("Hello2")
    public void greet(
            @EzyRequestData GreetRequest request,
            EzyUser user, EzySession session, Integer nothing) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }

    @EzyDoHandle("Hello3")
    public void greet(
            @EzyRequestData GreetRequest request,
            EzyUser user, EzySession session, int nothing) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }

    @EzyDoHandle("Hello4")
    public void greet(
            @EzyRequestData GreetRequest request,
            EzyUser user, EzySession session, boolean nothing) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }

    @EzyDoHandle("Hello5")
    public void greet(
            @EzyRequestData GreetRequest request,
            EzyUser user, EzySession session, char nothing) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }

    @EzyDoHandle("Hello6")
    public Object greetAndReturn(GreetRequest request) {
        return new GreetResponse("Hello " + request.getWho() + "!");
    }

    @EzyTryCatch({IllegalStateException.class, IllegalArgumentException.class})
    public void handleException1(Exception e) {
        e.printStackTrace();
    }

    @EzyTryCatch({RuntimeException.class})
    public void handleException2(
            RuntimeException e,
            GreetRequest request,
            EzyUser user, EzySession session, EzyContext context) {
        e.printStackTrace();
    }

    @EzyTryCatch({IllegalArgumentException.class})
    public void handleException3(
            IllegalArgumentException e,
            String cmd,
            GreetRequest request,
            EzyUser user, EzySession session, EzyContext context) {
        System.out.println("HelloController:handleException:IllegalArgumentException, cmd = " + cmd);
    }

}
