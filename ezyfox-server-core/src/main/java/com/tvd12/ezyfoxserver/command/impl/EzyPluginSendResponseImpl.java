package com.tvd12.ezyfoxserver.command.impl;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractChildSendResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginSendResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponseParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public class EzyPluginSendResponseImpl 
        extends EzyAbstractChildSendResponse<EzyPluginContext> 
        implements EzyPluginSendResponse {

    public EzyPluginSendResponseImpl(EzyPluginContext context) {
        super(context);
    }
    
    @Override
    public void execute(EzyData data, EzySession recipient) {
        EzyResponse response = newResponse(data);
        serverContext.send(response, recipient);
    }

    @Override
    public void execute(EzyData data, Collection<EzySession> recipients) {
        EzyResponse response = newResponse(data);
        serverContext.send(response, recipients);
    }
    
    protected EzyResponse newResponse(EzyData data) {
        EzyPluginSetting setting = context.getPlugin().getSetting();
        EzyRequestPluginResponseParams params = newResponseParams();
        params.setPluginId(setting.getId());
        params.setData(data);
        return new EzyRequestPluginResponse(params);
    }
    
    protected EzyRequestPluginResponseParams newResponseParams() {
        EzyRequestPluginResponseParams answer = new EzyRequestPluginResponseParams();
        return answer;
    }
}
