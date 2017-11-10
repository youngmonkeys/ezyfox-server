package com.tvd12.ezyfoxserver.plugincontroller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;

public class EzyUserRequestPluginController 
		extends EzyAbstractPluginEventController<EzyUserRequestPluginEvent> {

	@Override
	public void handle(EzyPluginContext ctx, EzyUserRequestPluginEvent event) {
		getLogger().info("plugin: user {} request", event.getUser().getName());
	}
	
}
