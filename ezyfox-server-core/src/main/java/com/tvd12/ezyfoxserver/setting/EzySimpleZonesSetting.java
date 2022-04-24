package com.tvd12.ezyfoxserver.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
@ToString
public class EzySimpleZonesSetting implements EzyZonesSetting {

    protected final List<EzyZoneSetting> zones
        = new ArrayList<>();
    protected final Map<Integer, EzySimpleZoneSetting> zonesByIds
        = new ConcurrentHashMap<>();
    protected final Map<String, EzySimpleZoneSetting> zonesByNames
        = new ConcurrentHashMap<>();

    @XmlElement(name = "zone")
    public void setItem(EzySimpleZoneSetting item) {
        zones.add(item);
        zonesByIds.put(item.getId(), item);
        zonesByNames.put(item.getName(), item);
    }

    @Override
    public Set<String> getZoneNames() {
        return zonesByNames.keySet();
    }

    @Override
    public Set<Integer> getZoneIds() {
        return zonesByIds.keySet();
    }

    @Override
    public EzySimpleZoneSetting getZoneByName(String name) {
        EzySimpleZoneSetting zoneSetting = zonesByNames.get(name);
        if (zoneSetting != null) {
            return zoneSetting;
        }
        throw new IllegalArgumentException("has no zone with name: " + name);
    }

    @Override
    public EzySimpleZoneSetting getZoneById(Integer id) {
        EzySimpleZoneSetting zoneSetting = zonesByIds.get(id);
        if (zoneSetting != null) {
            return zoneSetting;
        }
        throw new IllegalArgumentException("has no zone with id: " + id);
    }

    @Override
    public int getSize() {
        return zones.size();
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        List<Object> zoneMaps = new ArrayList<>();
        for (EzyZoneSetting zone : zones) {
            zoneMaps.add(zone.toMap());
        }
        map.put("size", zones.size());
        map.put("zones", zoneMaps);
        return map;
    }
}
