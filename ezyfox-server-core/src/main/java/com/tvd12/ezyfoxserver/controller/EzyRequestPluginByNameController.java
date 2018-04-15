package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginByNameParams;

public class EzyRequestPluginByNameController 
        extends EzyRequestPluginController<EzyRequestPluginByNameParams> {

    @Override
    protected EzyPluginContext getPluginContext(
            EzyZoneContext zoneCtx, EzyRequestPluginByNameParams requestParams) {
        String pluginName = requestParams.getPluginName();
        EzyPluginContext pluginContext = zoneCtx.getPluginContext(pluginName);
        return pluginContext;
    }
    
}
