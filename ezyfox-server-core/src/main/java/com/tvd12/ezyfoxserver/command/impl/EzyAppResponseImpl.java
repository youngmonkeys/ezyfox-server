package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractResponse;
import com.tvd12.ezyfoxserver.command.EzyAppResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
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
    protected void sendData(EzyData data, EzyTransportType transportType) {
        context.send(data, recipients, encrypted, transportType);
    }
    
}
