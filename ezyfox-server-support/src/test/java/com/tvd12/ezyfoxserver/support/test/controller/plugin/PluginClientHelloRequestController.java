package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;
import com.tvd12.ezyfoxserver.support.test.exception.RequestException4;

@EzyClientRequestController("plugin")
public class PluginClientHelloRequestController {

	@EzyRequestHandle("c_hello")
	public void handleHello(
			EzyPluginContext context,
			@EzyRequestData Hello data,
			EzyUser user) {
		System.out.println("plugin: c_hello: " + data.getWho());
	}
	
	@EzyRequestHandle("requestException4")
	public void handleRequestException4(
			EzyContext context, 
			String cmd, 
			@EzyRequestData Hello data) throws Exception {
		throw new RequestException4(getClass().getSimpleName() + ":handleRequestException4, cmd = " + cmd);
	}
	
	@EzyTryCatch({RequestException4.class})
	public void handleRequestException2(
			RequestException4 e, 
			String cmd,
			Hello request, 
			EzyUser user, EzySession session, EzyContext context) {
		System.out.println("PluginClientHelloRequestController::handleRequestException2, cmd = " + cmd + ", data = " + request + ", e = " + e);
	}
	
}
