package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyAbstractResponse;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponseParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
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
    protected EzyResponse newResponse() {
        EzyAppSetting setting = context.getApp().getSetting();
        EzyRequestAppResponseParams params = new EzyRequestAppResponseParams();
        params.setAppId(setting.getId());
        params.setData(newResponseData());
        return new EzyRequestAppResponse(params);
    }
    
}
