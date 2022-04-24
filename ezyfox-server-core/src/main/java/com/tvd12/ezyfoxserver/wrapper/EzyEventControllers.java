package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

import java.util.List;

public interface EzyEventControllers extends EzyDestroyable {

    @SuppressWarnings("rawtypes")
    List<EzyEventController> getControllers(EzyConstant eventType);

    @SuppressWarnings("rawtypes")
    void addController(EzyConstant eventType, EzyEventController controller);

}
