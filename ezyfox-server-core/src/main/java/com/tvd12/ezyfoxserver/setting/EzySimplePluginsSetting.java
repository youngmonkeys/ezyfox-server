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
@XmlRootElement(name = "plugins")
public class EzySimplePluginsSetting implements EzyPluginsSetting {

	protected final List<EzyPluginSetting> plugins = new ArrayList<>();
	protected final Map<Integer, EzySimplePluginSetting> pluginsByIds = new ConcurrentHashMap<>();
	protected final Map<String, EzySimplePluginSetting> pluginsByNames = new ConcurrentHashMap<>();
	
	@XmlElement(name = "plugin")
	public void setItem(EzySimplePluginSetting item) {
		plugins.add(item);
		pluginsByIds.put(item.getId(), item);
		pluginsByNames.put(item.getName(), item);
	}
	
	@Override
	public Set<String> getPluginNames() {
		return pluginsByNames.keySet();
	}
	
	@Override
	public Set<Integer> getPluginIds() {
		return pluginsByIds.keySet();
	}
	
	@Override
	public EzySimplePluginSetting getPluginByName(String name) {
	    EzySimplePluginSetting pluginSetting = pluginsByNames.get(name);
		if(pluginSetting != null)
			return pluginSetting;
		throw new IllegalArgumentException("has no plugin with name: " + name);
	}
	
	@Override
	public EzySimplePluginSetting getPluginById(Integer id) {
	    EzySimplePluginSetting pluginSetting = pluginsByIds.get(id);
		if(pluginSetting != null)
			return pluginSetting;
		throw new IllegalArgumentException("has no plugin with id: " + id);
	}
	
	@Override
	public int getSize() {
		return plugins.size();
	}
	
	public void setZoneId(int zoneId) {
	    plugins.forEach(p -> ((EzyZoneIdAware)p).setZoneId(zoneId));
	}
	
	@Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        List<Object> pluginMaps = new ArrayList<>();
        for(EzyPluginSetting plugin : plugins)
            pluginMaps.add(plugin.toMap());
        map.put("size", plugins.size());
        map.put("plugins", pluginMaps);
        return map;
    }
	
}
