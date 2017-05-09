package com.tvd12.ezyfoxserver.appcontroller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserReconnectEvent;

public class EzyUserReconnectController 
        extends EzyAbstractAppEventController<EzyUserReconnectEvent> {

    @Override
    public void handle(EzyAppContext ctx, EzyUserReconnectEvent event) {
        getLogger().info("app: user reconnect");
    }

}
