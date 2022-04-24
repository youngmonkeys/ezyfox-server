package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.Set;

public interface EzyZoneSetting extends EzyToMap {

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

    EzyEventControllersSetting getEventControllers();

}
