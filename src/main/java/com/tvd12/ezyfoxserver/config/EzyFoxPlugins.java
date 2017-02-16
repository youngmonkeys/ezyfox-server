package com.tvd12.ezyfoxserver.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
public class EzyFoxPlugins {

	private final List<EzyFoxPlugin> plugins;
	private final Map<Integer, EzyFoxPlugin> pluginsByIds;
	private final Map<String, EzyFoxPlugin> pluginsByNames;
	
	public EzyFoxPlugins() {
		this.plugins = new ArrayList<>();
		this.pluginsByIds = new HashMap<>();
		this.pluginsByNames = new HashMap<>();
	}
	
	@XmlElement(name = "plugin")
	public void setItems(final EzyFoxPlugin[] items) {
		for(EzyFoxPlugin item : items)
			setItem(item);
	}
	
	@XmlTransient
	private void setItem(final EzyFoxPlugin item) {
		plugins.add(item);
		pluginsByIds.put(item.getId(), item);
		pluginsByNames.put(item.getName(), item);
	}
	
	public Set<String> getPluginNames() {
		return pluginsByNames.keySet();
	}
	
	public Set<Integer> getPluginIds() {
		return pluginsByIds.keySet();
	}
	
	public EzyFoxPlugin getPluginByName(final String name) {
		if(pluginsByNames.containsKey(name))
			return pluginsByNames.get(name);
		throw new IllegalArgumentException("has no plugin with name: " + name);
	}
	
	public EzyFoxPlugin getPluginById(final Integer id) {
		if(pluginsByIds.containsKey(id))
			return pluginsByIds.get(id);
		throw new IllegalArgumentException("has no plugin with id: " + id);
	}
	
	public int getSize() {
		return plugins.size();
	}
	
}
