package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzySetup {

    @SuppressWarnings("rawtypes")
    EzySetup addEventController(
        EzyConstant eventType,
        EzyEventController controller
    );
}
