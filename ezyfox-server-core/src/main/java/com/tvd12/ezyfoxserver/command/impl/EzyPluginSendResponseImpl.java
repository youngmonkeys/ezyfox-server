package com.tvd12.ezyfoxserver.command.impl;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractChildSendResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginSendResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByIdResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByIdResponseParams;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByNameResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByNameResponseParams;
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
    public void execute(EzyData data, EzySession recipient, boolean withName) {
        EzyResponse response = newResponse(data, withName);
        serverContext.send(response, recipient);
    }

    @Override
    public void execute(EzyData data, Collection<EzySession> recipients, boolean withName) {
        EzyResponse response = newResponse(data, withName);
        serverContext.send(response, recipients);
    }
    
    protected EzyResponse newResponse(EzyData data, boolean withName) {
        EzyRequestPluginResponseParams params = newResponseParams(withName);
        params.setData(data);
        return withName 
                ? new EzyRequestPluginByNameResponse(params)
                : new EzyRequestPluginByIdResponse(params);
    }
    
    protected EzyRequestPluginResponseParams newResponseParams(boolean withName) {
        EzyPluginSetting setting = context.getPlugin().getSetting();
        if(withName) {
            EzyRequestPluginByNameResponseParams answer 
                    = new EzyRequestPluginByNameResponseParams();
            answer.setPluginName(setting.getName());
            return answer;
        }
        else {
            EzyRequestPluginByIdResponseParams answer 
                    = new EzyRequestPluginByIdResponseParams();
            answer.setPluginId(setting.getId());
            return answer;
        }
    }
}
