package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;

public interface EzyPluginSetup extends EzySetup {

    EzyPluginSetup setRequestController(EzyPluginRequestController controller);

    @SuppressWarnings("rawtypes")
    @Override
    EzyPluginSetup addEventController(
        EzyConstant eventType,
        EzyEventController controller
    );
}
