package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyEventControllerAdder;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public interface EzyServerChild extends EzyEventControllerAdder {

    EzyEventControllers getEventControllers();
    
}
