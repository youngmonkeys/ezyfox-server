package com.tvd12.ezyfoxserver.plugincontroller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

public class EzyServerReadyController 
		extends EzyAbstractPluginEventController<EzyServerReadyEvent> {

	@Override
	public void handle(EzyPluginContext ctx, EzyServerReadyEvent event) {
		getLogger().info("plugin: server ready controller");
	}
	
}
