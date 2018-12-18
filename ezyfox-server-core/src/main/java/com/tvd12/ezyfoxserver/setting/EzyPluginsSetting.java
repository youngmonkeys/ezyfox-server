package com.tvd12.ezyfoxserver.setting;

import java.util.List;
import java.util.Set;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyPluginsSetting extends EzyToMap {

    int getSize();
    
    List<EzyPluginSetting> getPlugins();

    Set<Integer> getPluginIds();
    
    Set<String> getPluginNames();
    
    EzyPluginSetting getPluginById(Integer id);

    EzyPluginSetting getPluginByName(String name);
    
}
