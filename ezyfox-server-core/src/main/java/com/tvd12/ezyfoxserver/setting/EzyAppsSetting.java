package com.tvd12.ezyfoxserver.setting;

import java.util.List;
import java.util.Set;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyAppsSetting extends EzyToMap {

    int getSize();
    
    List<EzyAppSetting> getApps();

    Set<Integer> getAppIds();
    
    Set<String> getAppNames();
    
    EzyAppSetting getAppById(Integer id);
    
    EzyAppSetting getAppByName(String name);
    
}
