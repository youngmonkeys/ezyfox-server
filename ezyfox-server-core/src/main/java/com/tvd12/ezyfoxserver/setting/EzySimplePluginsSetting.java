package com.tvd12.ezyfoxserver.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "plugins")
@JsonIgnoreProperties({"pluginsByIds", "pluginsByNames", "pluginNames", "pluginIds"})
@JsonPropertyOrder({"size", "plugins"})
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
		if(pluginsByNames.containsKey(name))
			return pluginsByNames.get(name);
		throw new IllegalArgumentException("has no plugin with name: " + name);
	}
	
	@Override
	public EzySimplePluginSetting getPluginById(Integer id) {
		if(pluginsByIds.containsKey(id))
			return pluginsByIds.get(id);
		throw new IllegalArgumentException("has no plugin with id: " + id);
	}
	
	@Override
	public int getSize() {
		return plugins.size();
	}
	
	public void setZoneId(int zoneId) {
	    plugins.forEach(p -> ((EzyZoneIdAware)p).setZoneId(zoneId));
	}
	
}
