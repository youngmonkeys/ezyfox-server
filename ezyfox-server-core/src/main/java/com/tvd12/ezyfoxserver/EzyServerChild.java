package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzyServerChild extends EzyEventComponent {

    @SuppressWarnings("rawtypes")
    void addEventController(EzyConstant eventType, EzyEventController ctrl);
}
