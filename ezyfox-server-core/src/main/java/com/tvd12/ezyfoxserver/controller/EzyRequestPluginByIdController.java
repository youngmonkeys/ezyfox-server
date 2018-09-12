package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginByIdParams;

public class EzyRequestPluginByIdController 
        extends EzyRequestPluginController<EzyRequestPluginByIdParams> {

    @Override
    protected EzyPluginContext getPluginContext(
            EzyZoneContext zoneCtx, EzyRequestPluginByIdParams requestParams) {
        int pluginId = requestParams.getPluginId();
        EzyPluginContext pluginContext = zoneCtx.getPluginContext(pluginId);
        return pluginContext;
    }
    
    @Override
    protected boolean withName() {
        return false;
    }
}
