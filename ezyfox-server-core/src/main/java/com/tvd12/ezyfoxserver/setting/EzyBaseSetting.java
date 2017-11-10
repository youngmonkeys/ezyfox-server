package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public interface EzyBaseSetting extends EzyEventControllerAdder {

    int getId();

    String getName();

    String getLocation();
    
    String getEntryLoader();
    
    int getWorkerPoolSize();
    
    String getConfigFile();
    
    EzyEventControllers getEventControllers();

}
