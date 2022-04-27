package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestPluginHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzySingleton
@EzyRequestListener("hello")
public class PluginClientHelloRequestHandler
    implements EzyUserRequestPluginHandler<Hello> {

    @Override
    public void handle(EzyPluginContext context, EzyUserSessionEvent event, Hello data) {
        System.out.println("hello: " + data.getWho());
    }
}
