package com.tvd12.ezyfoxserver.support.test.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import com.tvd12.ezyfoxserver.support.test.data.GreetRequest;
import com.tvd12.ezyfoxserver.support.test.data.GreetResponse;

@EzyClientRequestController("Big")
public class HelloController {

	@EzyAutoBind
	protected EzyResponseFactory appResponseFactory;
	
	@EzyRequestHandle("Hello")
	public void greet(GreetRequest request, EzyUser user, EzySession session) {
		GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
		System.out.println("HelloController::Big/Hello response: " + response);
	}
	
}
