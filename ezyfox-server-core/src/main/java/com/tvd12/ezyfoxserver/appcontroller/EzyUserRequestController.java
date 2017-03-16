package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyRequestAppEvent;

public class EzyUserRequestController 
		extends EzyAbstractAppEventController<EzyRequestAppEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyRequestAppEvent event) {
		getLogger().info("user request controller");
	}
	
}
