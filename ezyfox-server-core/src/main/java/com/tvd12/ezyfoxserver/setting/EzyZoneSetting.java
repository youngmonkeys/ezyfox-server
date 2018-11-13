package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public interface EzyZoneSetting {

    int getId();

    String getName();

    String getConfigFile();

    int getMaxUsers();
    
    EzyStreamingSetting getStreaming();

    EzyPluginsSetting getPlugins();

    EzyAppsSetting getApplications();

    Set<Integer> getAppIds();

    Set<String> getAppNames();

    EzyAppSetting getAppById(Integer id);

    EzyAppSetting getAppByName(String name);

    Set<Integer> getPluginIds();

    Set<String> getPluginNames();

    EzyPluginSetting getPluginById(Integer id);

    EzyPluginSetting getPluginByName(String name);
    
    EzyUserManagementSetting getUserManagement();
    
    EzyEventPluginsMapper getEventPluginsMapper();
    
    EzyEventControllersSetting getEventControllers();

}
