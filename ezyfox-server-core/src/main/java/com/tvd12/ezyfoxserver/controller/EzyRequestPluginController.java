package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
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
	    EzyPlugin plugin = pluginCtx.getPlugin();
	    EzyPluginRequestController requestController = plugin.getRequestController();
	    EzyUserRequestPluginEvent event = newRequestPluginEvent(request);
        requestController.handle(pluginCtx, event);
	}
	
	protected abstract 
	    EzyPluginContext getPluginContext(EzyZoneContext zoneCtx, P requestParams);
	
	protected EzyUserRequestPluginEvent newRequestPluginEvent(EzyRequestPluginRequest<P> request) {
		return new EzySimpleUserRequestPluginEvent(
		        request.getUser(),
		        request.getSession(), 
		        request.getParams().getData());
	}
	
}
