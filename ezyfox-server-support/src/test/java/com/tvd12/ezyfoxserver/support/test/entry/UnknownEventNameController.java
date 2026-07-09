package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

@EzyEventHandler("NOT_A_REAL_EVENT")
public class UnknownEventNameController
    implements EzyEventController<Object, Object> {

    @Override
    public void handle(Object ctx, Object event) {}
}
