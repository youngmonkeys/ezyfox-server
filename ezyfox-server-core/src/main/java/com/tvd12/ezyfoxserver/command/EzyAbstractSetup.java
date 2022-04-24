package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyServerChild;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class EzyAbstractSetup extends EzyAbstractCommand {

    private final EzyServerChild serverChild;
    
    @SuppressWarnings("rawtypes")
    public void addEventController0(EzyConstant eventType, EzyEventController controller) {
        serverChild.addEventController(eventType, controller);
    }
    
}
