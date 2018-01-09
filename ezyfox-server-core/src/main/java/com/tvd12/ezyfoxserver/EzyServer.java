package com.tvd12.ezyfoxserver;

import java.util.Map;

import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;

public interface EzyServer extends 
        EzyAppIdsFetcher, 
        EzyAppByIdFetcher, 
        EzyPluginIdsFetcher, 
        EzyPluginByIdFetcher {
    
    String getVersion();

    EzyConfig getConfig();
    
    EzySettings getSettings();
    
    EzyManagers getManagers();
    
    ClassLoader getClassLoader();
    
    EzyStatistics getStatistics();
    
    EzyJsonMapper getJsonMapper();
    
    EzyServerControllers getControllers();
    
    EzyEventPluginsMapper getEventPluginsMapper();
    
    Map<String, EzyAppClassLoader> getAppClassLoaders();
    
}
