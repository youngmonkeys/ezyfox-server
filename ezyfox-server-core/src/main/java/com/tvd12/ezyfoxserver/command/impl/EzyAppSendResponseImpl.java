package com.tvd12.ezyfoxserver.command.impl;

import java.util.Collection;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractChildSendResponse;
import com.tvd12.ezyfoxserver.command.EzyAppSendResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponse;
import com.tvd12.ezyfoxserver.response.EzyRequestAppResponseParams;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public class EzyAppSendResponseImpl 
        extends EzyAbstractChildSendResponse<EzyAppContext> 
        implements EzyAppSendResponse {

    public EzyAppSendResponseImpl(EzyAppContext context) {
        super(context);
    }
    
    @Override
    public void execute(
            EzyData data, 
            EzySession recipient, EzyTransportType transportType) {
        EzyResponse response = newResponse(data);
        serverContext.send(response, recipient, transportType);
    }

    @Override
    public void execute(
            EzyData data, 
            Collection<EzySession> recipients, EzyTransportType transportType) {
        EzyResponse response = newResponse(data);
        serverContext.send(response, recipients, transportType);
    }
    
    protected EzyResponse newResponse(EzyData data) {
        EzyAppSetting setting = context.getApp().getSetting();
        EzyRequestAppResponseParams params = new EzyRequestAppResponseParams();
        params.setAppId(setting.getId());
        params.setData(data);
        return new EzyRequestAppResponse(params);
    }
}
