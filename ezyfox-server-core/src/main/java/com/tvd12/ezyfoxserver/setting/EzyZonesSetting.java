package com.tvd12.ezyfoxserver.setting;

import java.util.List;
import java.util.Set;

public interface EzyZonesSetting {

    int getSize();
    
    List<EzyZoneSetting> getZones();

    Set<Integer> getZoneIds();
    
    Set<String> getZoneNames();
    
    EzyZoneSetting getZoneById(Integer id);

    EzyZoneSetting getZoneByName(String name);
    
}
