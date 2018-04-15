package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByIdResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByIdResponseParams;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByNameResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginByNameResponseParams;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponseParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyPluginResponseImpl 
        extends EzyAbstractResponse<EzyPluginContext> 
        implements EzyPluginResponse {
    
    private boolean withName = true;

    public EzyPluginResponseImpl(EzyPluginContext context) {
        super(context);
    } 
    
    @Override
    public void withName(boolean value) {
        this.withName = value;
    }
    
    @Override
    protected EzyUserManager getUserManager(EzyPluginContext context) {
        return EzyZoneContexts.getUserManager(context.getParent());
    }
     
    @Override
    protected EzyResponse newResponse() {
        EzyRequestPluginResponseParams params = newResponseParams();
        params.setData(newResponseData());
        return withName 
                ? new EzyRequestPluginByNameResponse(params)
                : new EzyRequestPluginByIdResponse(params);
    }
    
    protected EzyRequestPluginResponseParams newResponseParams() {
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
