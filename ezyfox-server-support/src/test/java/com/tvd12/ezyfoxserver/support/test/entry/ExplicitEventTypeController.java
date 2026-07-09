package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public class ExplicitEventTypeController
    implements EzyEventController<Object, Object> {

    @Override
    public EzyConstant getEventType() {
        return EzyCustomEventType.CUSTOM_EVENT;
    }

    @Override
    public void handle(Object ctx, Object event) {}
}
