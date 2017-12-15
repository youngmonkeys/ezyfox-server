package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.context.EzyServerContexts;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.response.EzyRequestPluginResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse.Builder;
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
    protected Builder newResponseBuilder(EzyData data) {
        return newResponseBuilder(context.getPlugin().getSetting(), data);
    }
    
    protected Builder newResponseBuilder(EzyPluginSetting setting, EzyData data) {
        return EzyRequestPluginResponse.builder()
                .pluginName(setting.getName())
                .data(data);
    }

}
