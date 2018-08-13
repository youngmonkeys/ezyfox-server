package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;

public class EzyUserRemovedController 
		extends EzyAbstractAppEventController<EzyUserRemovedEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyUserRemovedEvent event) {
		getLogger().info("user {} removed", event.getUser().getName());
	}
	
}
