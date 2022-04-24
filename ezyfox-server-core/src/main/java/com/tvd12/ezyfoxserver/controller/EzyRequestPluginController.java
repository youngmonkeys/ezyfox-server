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

public class EzyRequestPluginController 
        extends EzyAbstractServerController
        implements EzyServerController<EzyRequestPluginRequest> {

    @Override
    public void handle(EzyServerContext ctx, EzyRequestPluginRequest request) {
        EzyRequestPluginParams params = request.getParams();
        EzyUser user = request.getUser();
        EzyZoneContext zoneCtx = ctx.getZoneContext(user.getZoneId());
        EzyPluginContext pluginCtx = getPluginContext(zoneCtx, params);
        EzyPlugin plugin = pluginCtx.getPlugin();
        EzyPluginRequestController requestController = plugin.getRequestController();
        EzyUserRequestPluginEvent event = newRequestPluginEvent(request);
        requestController.handle(pluginCtx, event);
    }

    protected EzyPluginContext getPluginContext(
            EzyZoneContext zoneCtx, EzyRequestPluginParams requestParams) {
        int pluginId = requestParams.getPluginId();
        EzyPluginContext pluginContext = zoneCtx.getPluginContext(pluginId);
        return pluginContext;
    }

    protected EzyUserRequestPluginEvent newRequestPluginEvent(EzyRequestPluginRequest request) {
        return new EzySimpleUserRequestPluginEvent(
                request.getUser(),
                request.getSession(),
                request.getParams().getData());
    }

}
