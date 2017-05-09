package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse.Builder;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyAppResponseImpl 
        extends EzyAbstractResponse<EzyAppContext> 
        implements EzyAppResponse {

    public EzyAppResponseImpl(EzyAppContext context) {
        super(context);
    }
    
    @Override
    protected EzyUserManager getUserManager(EzyAppContext context) {
        return context.getApp().getUserManager();
    }
    
    @Override
    protected Builder newResponseBuilder(EzyData data) {
        return newResponseBuilder(context.getApp().getSetting(), data);
    }
    
    protected Builder newResponseBuilder(EzyAppSetting setting, EzyData data) {
        return EzyRequestAppResponse.builder()
                .appId(setting.getId())
                .data(data);
    }

}
