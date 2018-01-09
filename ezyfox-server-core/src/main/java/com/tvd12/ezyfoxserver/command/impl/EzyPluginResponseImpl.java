package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyPluginResponseImpl 
        extends EzyAbstractResponse<EzyPluginContext> 
        implements EzyPluginResponse {

    public EzyPluginResponseImpl(EzyPluginContext context) {
        super(context);
    } 
    
    @Override
    protected EzyUserManager getUserManager(EzyPluginContext context) {
        return EzyServerContexts.getUserManager(context.getParent());
    }
     
    @Override
    protected EzyResponse newResponse() {
        EzyPluginSetting setting = context.getPlugin().getSetting();
        EzyRequestPluginParams params = new EzyRequestPluginParams();
        params.setPluginName(setting.getName());
        params.setData(newResponseData());
        return new EzyRequestPluginResponse(params);
    }
    

}
