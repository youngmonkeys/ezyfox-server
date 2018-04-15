package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public interface EzyBaseSetting extends EzyEventControllerAdder {

    int getId();
    
    String getName();
    
    String getFolder();
    
    int getZoneId();

    String getLocation();
    
    String getEntryLoader();
    
    int getThreadPoolSize();
    
    String getConfigFile();
    
    EzyEventControllers getEventControllers();

}
