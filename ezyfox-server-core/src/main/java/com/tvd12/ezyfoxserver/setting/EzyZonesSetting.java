package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.List;
import java.util.Set;

public interface EzyZonesSetting extends EzyToMap {

    int getSize();

    List<EzyZoneSetting> getZones();

    Set<Integer> getZoneIds();

    Set<String> getZoneNames();

    EzyZoneSetting getZoneById(Integer id);

    EzyZoneSetting getZoneByName(String name);
}
