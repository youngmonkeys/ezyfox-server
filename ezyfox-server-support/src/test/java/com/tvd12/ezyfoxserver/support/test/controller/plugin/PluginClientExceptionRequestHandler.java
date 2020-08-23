package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzySingleton
@EzyClientRequestListener("exception")
public class PluginClientExceptionRequestHandler 
		implements EzyUserRequestHandler<EzyPluginContext, Hello> {

	@Override
	public void handle(EzyPluginContext context, EzyUserSessionEvent event, Hello data) {
		throw new IllegalStateException("server maintain");
	}

}
