package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.request.EzyPluginInfoParams;
import com.tvd12.ezyfoxserver.request.EzyPluginInfoRequest;
import com.tvd12.ezyfoxserver.response.EzyPluginInfoResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public class EzyPluginInfoController 
        extends EzyAbstractServerController
        implements EzyServerController<EzyPluginInfoRequest> {

    @Override
    public void handle(EzyServerContext ctx, EzyPluginInfoRequest request) {
        EzyUser user = request.getUser();
        EzySession session = request.getSession();
        EzyPluginInfoParams params = request.getParams();
        EzyZoneContext zoneCtx = ctx.getZoneContext(user.getZoneId());
        EzyPluginContext pluginCtx = zoneCtx.getPluginContext(params.getPluginName());
        if(pluginCtx != null) {
            EzyPluginSetting setting = pluginCtx.getPlugin().getSetting();
            EzyResponse response = newPluginInfoResponse(setting);
            ctx.send(response, session, false);
        }
    }

    protected EzyResponse newPluginInfoResponse(EzyPluginSetting pluginSetting) {
        com.tvd12.ezyfoxserver.response.EzyPluginInfoParams params = 
                new com.tvd12.ezyfoxserver.response.EzyPluginInfoParams();
        params.setPlugin(pluginSetting);
        return new EzyPluginInfoResponse(params);
    }

}
