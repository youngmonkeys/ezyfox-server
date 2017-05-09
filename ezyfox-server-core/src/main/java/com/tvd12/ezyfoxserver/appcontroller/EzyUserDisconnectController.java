package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserDisconnectEvent;

public class EzyUserDisconnectController 
		extends EzyAbstractAppEventController<EzyUserDisconnectEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyUserDisconnectEvent event) {
		getLogger().info("user {} disconnected", event.getUser().getName());
	}
	
}
