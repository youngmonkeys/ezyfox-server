package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public interface EzyZone extends 
        EzyAppIdsFetcher, 
        EzyAppByIdFetcher, 
        EzyPluginIdsFetcher, 
        EzyPluginByIdFetcher {

    EzyZoneSetting getSetting();
    
    EzyZoneUserManager getUserManager();
    
    EzyEventPluginsMapper getEventPluginsMapper();
    
}
