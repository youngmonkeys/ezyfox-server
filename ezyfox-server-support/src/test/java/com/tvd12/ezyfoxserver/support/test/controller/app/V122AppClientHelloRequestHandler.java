package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;

@EzyManagement
@EzyPayment
@EzySingleton
@EzyFeature("hello.world")
@EzyRequestListener("v122/listener/hello")
public class V122AppClientHelloRequestHandler
    implements EzyUserRequestHandler<EzyAppContext, Hello> {

    @Override
    public void handle(EzyAppContext context, EzyUserSessionEvent event, Hello data) {
        System.out.println("hello: " + data.getWho());
    }
}
