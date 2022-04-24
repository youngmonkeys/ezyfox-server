package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestAppHandler;

@SuppressWarnings("rawtypes")
@EzySingleton
@EzyRequestListener("hello4")
public class AppClientHello4RequestHandler 
        implements EzyUserRequestAppHandler {
    
    @Override
    public void handle(EzyContext context, EzyUserSessionEvent event, Object data) {
        System.out.println("hello with: " + data);
    }

}
