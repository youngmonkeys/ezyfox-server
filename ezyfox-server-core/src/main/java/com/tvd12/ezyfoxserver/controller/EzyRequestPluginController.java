package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginParams;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginRequest;

public abstract class EzyRequestPluginController<P extends EzyRequestPluginParams> 
		extends EzyAbstractServerController 
		implements EzyServerController<EzyRequestPluginRequest<P>> {

	@Override
	public void handle(EzyServerContext ctx, EzyRequestPluginRequest<P> request) {
	    P params = request.getParams();
	    EzyUser user = request.getUser();
	    EzyZoneContext zoneCtx = ctx.getZoneContext(user.getZoneId());
	    EzyPluginContext pluginCtx = getPluginContext(zoneCtx, params);
        EzyEvent event = newRequestPluginEvent(request);
        EzyEventType type = EzyEventType.USER_REQUEST;
        pluginCtx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected abstract 
	    EzyPluginContext getPluginContext(EzyZoneContext zoneCtx, P requestParams);
	
	protected EzyEvent newRequestPluginEvent(EzyRequestPluginRequest<P> request) {
		return EzySimpleUserRequestPluginEvent.builder()
		        .user(request.getUser())
		        .session(request.getSession())
		        .data(request.getParams().getData())
		        .build();
	}
	
}
