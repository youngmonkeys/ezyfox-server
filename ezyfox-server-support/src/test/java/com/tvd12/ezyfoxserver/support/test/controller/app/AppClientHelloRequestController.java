package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzyClientRequestController
public class AppClientHelloRequestController {

	@EzyRequestHandle("c_hello")
	public void handleHello(EzyAppContext context, EzyUserSessionEvent event, Hello data) {
		System.out.println("c_hello: " + data.getWho());
	}
	
}
