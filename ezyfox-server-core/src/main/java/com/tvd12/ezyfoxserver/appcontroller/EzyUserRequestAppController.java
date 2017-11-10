package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class EzyUserRequestAppController 
		extends EzyAbstractAppEventController<EzyUserRequestAppEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyUserRequestAppEvent event) {
		getLogger().info("user request controller");
	}
	
}
