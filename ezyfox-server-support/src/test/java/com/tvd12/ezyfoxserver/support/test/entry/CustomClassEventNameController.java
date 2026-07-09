package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

@EzyEventHandler(
    "com.tvd12.ezyfoxserver.support.test.entry.EzyCustomEventType.CUSTOM_EVENT"
)
public class CustomClassEventNameController
    implements EzyEventController<Object, Object> {

    @Override
    public void handle(Object ctx, Object event) {}
}
