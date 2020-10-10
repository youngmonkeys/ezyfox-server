package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.core.annotation.EzyClientRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyRequestHandle;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzyClientRequestController("plugin")
public class PluginClientHelloRequestController {

	@EzyRequestHandle("c_hello")
	public void handleHello(
			EzyPluginContext context,
			@EzyRequestData Hello data,
			EzyUser user) {
		System.out.println("plugin: c_hello: " + data.getWho());
	}
	
}
