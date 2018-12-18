package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyBaseSetting extends EzyToMap {

    int getId();
    
    String getName();
    
    String getFolder();
    
    int getZoneId();

    String getLocation();
    
    String getEntryLoader();
    
    int getThreadPoolSize();
    
    String getConfigFile();
    
}
