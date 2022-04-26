package com.tvd12.ezyfoxserver.embedded.test.plugin;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

@EzySingleton
@EzyEventHandler(event = EzyEventNames.SERVER_READY)
public class HelloPluginServerReadyController
    extends EzyAbstractPluginEventController<EzyServerReadyEvent> {

    @Override
    public void handle(EzyPluginContext ctx, EzyServerReadyEvent event) {
        logger.info("HELLO PLUGIN - SERVER READY");
    }
}
