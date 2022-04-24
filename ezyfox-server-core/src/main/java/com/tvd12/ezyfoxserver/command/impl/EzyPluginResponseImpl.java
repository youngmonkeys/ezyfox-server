package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractResponse;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContexts;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzyPluginResponseImpl
    extends EzyAbstractResponse<EzyPluginContext>
    implements EzyPluginResponse {

    public EzyPluginResponseImpl(EzyPluginContext context) {
        super(context);
    }

    @Override
    protected EzyUserManager getUserManager(EzyPluginContext context) {
        return EzyZoneContexts.getUserManager(context.getParent());
    }

    @Override
    protected void sendData(EzyData data, EzyTransportType transportType) {
        context.send(data, recipients, encrypted, transportType);
    }
}
