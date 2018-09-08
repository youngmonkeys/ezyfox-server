package com.tvd12.ezyfoxserver.setting;

public interface EzyBaseSetting {

    int getId();
    
    String getName();
    
    String getFolder();
    
    int getZoneId();

    String getLocation();
    
    String getEntryLoader();
    
    int getThreadPoolSize();
    
    String getConfigFile();

}
