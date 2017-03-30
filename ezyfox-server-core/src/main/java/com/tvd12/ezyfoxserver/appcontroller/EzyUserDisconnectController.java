package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyDisconnectEvent;

public class EzyUserDisconnectController 
		extends EzyAbstractAppEventController<EzyDisconnectEvent> {

	@Override
	public void handle(EzyAppContext ctx, EzyDisconnectEvent event) {
		getLogger().info("user {} disconnected", event.getUser().getName());
	}
	
}
