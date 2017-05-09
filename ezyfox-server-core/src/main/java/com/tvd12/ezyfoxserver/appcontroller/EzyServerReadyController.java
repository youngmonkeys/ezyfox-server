package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

public class EzyServerReadyController 
		extends EzyAbstractAppEventController<EzyServerReadyEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyServerReadyEvent event) {
		getLogger().info("app: server ready controller");
	}
	
}
