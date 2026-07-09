package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

@EzyEventHandler("USER_LOGIN")
public class BuiltInEventNameController
    implements EzyEventController<Object, Object> {

    @Override
    public void handle(Object ctx, Object event) {}
}
