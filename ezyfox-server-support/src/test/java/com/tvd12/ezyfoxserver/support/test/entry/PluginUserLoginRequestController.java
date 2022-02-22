package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

@EzyEventHandler(EzyEventNames.USER_LOGIN)
public class PluginUserLoginRequestController
    extends EzyAbstractPluginEventController<EzyUserLoginEvent> {

    @Override
    public void handle(EzyPluginContext ctx, EzyUserLoginEvent event) {}
}
