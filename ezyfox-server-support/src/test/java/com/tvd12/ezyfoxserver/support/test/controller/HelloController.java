package com.tvd12.ezyfoxserver.support.test.controller;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.context.EzyContext;
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
	
	@EzyRequestHandle("Hello2")
	public void greet(
			@EzyRequestData GreetRequest request, 
			EzyUser user, EzySession session, Integer nothing) {
		GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
		System.out.println("HelloController::Big/Hello response: " + response);
	}
	
	@EzyRequestHandle("Hello3")
	public void greet(
			@EzyRequestData GreetRequest request, 
			EzyUser user, EzySession session, int nothing) {
		GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
		System.out.println("HelloController::Big/Hello response: " + response);
	}
	
	@EzyRequestHandle("Hello4")
	public void greet(
			@EzyRequestData GreetRequest request, 
			EzyUser user, EzySession session, boolean nothing) {
		GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
		System.out.println("HelloController::Big/Hello response: " + response);
	}
	
	@EzyRequestHandle("Hello5")
	public void greet(
			@EzyRequestData GreetRequest request, 
			EzyUser user, EzySession session, char nothing) {
		GreetResponse response = new GreetResponse("Hello " + request.getWho() + "!");
		System.out.println("HelloController::Big/Hello response: " + response);
	}
	
	@EzyTryCatch({IllegalStateException.class, IllegalArgumentException.class})
	public void handleException(Exception e) {
		e.printStackTrace();
	}
	
	@EzyTryCatch({RuntimeException.class})
	public void handleException(
			RuntimeException e, 
			GreetRequest request, 
			EzyUser user, EzySession session, EzyContext context) {
		e.printStackTrace();
	}
	
}
