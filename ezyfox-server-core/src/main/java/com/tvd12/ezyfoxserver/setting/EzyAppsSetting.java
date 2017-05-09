package com.tvd12.ezyfoxserver.setting;

import java.util.List;
import java.util.Set;

public interface EzyAppsSetting {

    int getSize();
    
    List<EzyAppSetting> getApps();

    Set<Integer> getAppIds();
    
    Set<String> getAppNames();
    
    EzyAppSetting getAppById(Integer id);
    
    EzyAppSetting getAppByName(String name);
    
}
