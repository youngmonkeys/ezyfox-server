package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServerChild;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class EzyAbstractSetup extends EzyAbstractCommand {

    private final EzyServerChild serverChild;

    @SuppressWarnings("rawtypes")
    protected void doAddEventController(
        EzyConstant eventType,
        EzyEventController controller
    ) {
        serverChild.addEventController(eventType, controller);
    }
}
