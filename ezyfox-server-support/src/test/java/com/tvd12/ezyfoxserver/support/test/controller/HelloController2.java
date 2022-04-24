package com.tvd12.ezyfoxserver.support.test.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;
import com.tvd12.ezyfoxserver.support.test.data.GreetResponse;

@EzyRequestController("Big")
public class HelloController2 {

    @EzyAutoBind
    protected EzyResponseFactory appResponseFactory;
    
    @EzyDoHandle("Hello")
    public void greet(GreetRequest request, EzyUser user, EzySession session) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }
    
    @EzyDoHandle("Hello")
    public void greet(
            @EzyRequestData GreetRequest request, 
            EzyUser user, EzySession session, Integer nothing) {
        GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
        System.out.println("HelloController::Big/Hello response: " + response);
    }
}
