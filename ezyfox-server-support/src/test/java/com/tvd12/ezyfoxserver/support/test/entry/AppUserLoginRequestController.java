package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

@EzyEventHandler(EzyEventNames.USER_LOGIN)
public class AppUserLoginRequestController
    extends EzyAbstractAppEventController<EzyUserLoginEvent> {

    @Override
    public void handle(EzyAppContext ctx, EzyUserLoginEvent event) {}
}
