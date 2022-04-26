package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzyAppSetup extends EzySetup {

    EzyAppSetup setRequestController(EzyAppRequestController controller);

    @SuppressWarnings("rawtypes")
    @Override
    EzyAppSetup addEventController(
        EzyConstant eventType,
        EzyEventController controller
    );
}
