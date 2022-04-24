package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

@SuppressWarnings("rawtypes")
public interface EzyAppSetup extends EzySetup {

    EzyAppSetup setRequestController(EzyAppRequestController controller);

    @Override
    EzyAppSetup addEventController(EzyConstant eventType, EzyEventController controller);

}
