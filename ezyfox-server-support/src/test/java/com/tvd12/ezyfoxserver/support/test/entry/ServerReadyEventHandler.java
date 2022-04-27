package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;

@EzySingleton
@EzyEventHandler(value = EzyEventNames.SERVER_READY, priority = Integer.MAX_VALUE)
public class ServerReadyEventHandler implements EzyServerReadyController {

    @Override
    public void handle(EzyServerContext ctx, EzyServerReadyEvent event) {}
}
