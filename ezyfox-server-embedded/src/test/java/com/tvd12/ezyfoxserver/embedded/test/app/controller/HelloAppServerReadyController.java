package com.tvd12.ezyfoxserver.embedded.test.app.controller;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

@EzySingleton
@EzyEventHandler(event = EzyEventNames.SERVER_READY)
public class HelloAppServerReadyController 
        extends EzyAbstractAppEventController<EzyServerReadyEvent> {

    @Override
    public void handle(EzyAppContext ctx, EzyServerReadyEvent event) {
        logger.info("HELLO APP - SERVER READY");
    }

}
