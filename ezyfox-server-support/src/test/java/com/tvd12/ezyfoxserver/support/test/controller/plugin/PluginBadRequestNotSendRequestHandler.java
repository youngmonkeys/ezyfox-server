package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzySingleton
@EzyRequestListener("badRequestNotSend")
public class PluginBadRequestNotSendRequestHandler 
        implements EzyUserRequestHandler<EzyPluginContext, Hello> {

    @Override
    public void handle(EzyPluginContext context, EzyUserSessionEvent event, Hello data) {
        throw new EzyBadRequestException(1, "server maintain", false);
    }

}
