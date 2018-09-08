package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public interface EzyServerChild {

    EzyEventControllers getEventControllers();
    
    @SuppressWarnings("rawtypes")
    void addEventController(EzyConstant eventType, EzyEventController ctrl);
    
}
