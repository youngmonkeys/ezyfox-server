package com.tvd12.ezyfoxserver.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "applications")
public class EzySimpleAppsSetting implements EzyAppsSetting {

	protected final List<EzyAppSetting> apps = new ArrayList<>();
	protected final Map<Integer, EzySimpleAppSetting> appsByIds = new ConcurrentHashMap<>();
	protected final Map<String, EzySimpleAppSetting> appsByNames = new ConcurrentHashMap<>();
	
	@XmlElement(name = "application")
	public void setItem(EzySimpleAppSetting item) {
		apps.add(item);
		appsByIds.put(item.getId(), item);
		appsByNames.put(item.getName(), item);
	}
	
	@Override
	public Set<String> getAppNames() {
		return appsByNames.keySet();
	}
	
	@Override
	public Set<Integer> getAppIds() {
		return appsByIds.keySet();
	}
	
	@Override
	public EzySimpleAppSetting getAppByName(String name) {
	    EzySimpleAppSetting appSetting = appsByNames.get(name);
		if(appSetting != null)
			return appSetting;
		throw new IllegalArgumentException("has no app with name: " + name);
	}
	
	@Override
	public EzySimpleAppSetting getAppById(Integer id) {
	    EzySimpleAppSetting appSetting = appsByIds.get(id);
		if(appSetting != null)
			return appSetting;
		throw new IllegalArgumentException("has no app with id: " + id);
	}
	
	@Override
	public int getSize() {
		return apps.size();
	}
	
	public void setZoneId(int zoneId) {
        apps.forEach(a -> ((EzyZoneIdAware)a).setZoneId(zoneId));
    }
	
	@Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        List<Object> appMaps = new ArrayList<>();
        for(EzyAppSetting app : apps)
            appMaps.add(app.toMap());
        map.put("size", apps.size());
        map.put("apps", appMaps);
        return map;
    }
	
}
