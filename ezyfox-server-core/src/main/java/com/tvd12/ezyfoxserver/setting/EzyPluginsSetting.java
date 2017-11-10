package com.tvd12.ezyfoxserver.setting;

import java.util.List;
import java.util.Set;

public interface EzyPluginsSetting {

    int getSize();
    
    List<EzyPluginSetting> getPlugins();

    Set<Integer> getPluginIds();
    
    Set<String> getPluginNames();
    
    EzyPluginSetting getPluginById(Integer id);

    EzyPluginSetting getPluginByName(String name);
    
}
