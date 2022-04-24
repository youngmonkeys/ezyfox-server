package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestPluginHandler;

@SuppressWarnings("rawtypes")
@EzySingleton
@EzyRequestListener("hello3")
public class PluginClientHello3RequestHandler 
        implements EzyUserRequestPluginHandler {

    @Override
    public void handle(EzyContext context, EzyUserSessionEvent event, Object data) {
        System.out.println("hello: " + data);
    }

}
