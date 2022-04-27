package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.command.EzyPluginResponse;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;

import java.util.HashMap;
import java.util.Map;

public class EzyPluginObjectResponse extends EzyAbstractObjectResponse {

    protected Map<Object, Object> additionalParams = new HashMap<>();

    public EzyPluginObjectResponse(EzyPluginContext context, EzyMarshaller marshaller) {
        super(context, marshaller);
    }

    @Override
    protected EzyResponse newResponse() {
        return context.cmd(EzyPluginResponse.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.additionalParams.clear();
        this.additionalParams = null;
    }
}
