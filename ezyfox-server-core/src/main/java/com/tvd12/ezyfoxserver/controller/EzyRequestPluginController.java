package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginParams;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginRequest;

public class EzyRequestPluginController 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyRequestPluginRequest> {

	@Override
	public void handle(EzyServerContext ctx, EzyRequestPluginRequest request) {
	    EzyRequestPluginParams params = request.getParams();
	    EzyPluginContext pluginCtx = ctx.getPluginContext(params.getPluginName());
        EzyEvent event = newRequestPluginEvent(request);
        EzyEventType type = EzyEventType.USER_REQUEST;
        pluginCtx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected EzyEvent newRequestPluginEvent(EzyRequestPluginRequest request) {
		return EzySimpleUserRequestPluginEvent.builder()
		        .user(request.getUser())
		        .session(request.getSession())
		        .data(request.getParams().getData())
		        .build();
	}
	
}
