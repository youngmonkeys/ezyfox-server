package com.tvd12.ezyfoxserver.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
public class EzyPlugins {

	protected final List<EzyPlugin> plugins;
	protected final Map<Integer, EzyPlugin> pluginsByIds;
	protected final Map<String, EzyPlugin> pluginsByNames;
	
	public EzyPlugins() {
		this.plugins = new ArrayList<>();
		this.pluginsByIds = new ConcurrentHashMap<>();
		this.pluginsByNames = new ConcurrentHashMap<>();
	}
	
	@XmlElement(name = "plugin")
	public void setItems(final EzyPlugin[] items) {
		for(EzyPlugin item : items)
			setItem(item);
	}
	
	@XmlTransient
	private void setItem(final EzyPlugin item) {
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
	
	public EzyPlugin getPluginByName(final String name) {
		if(pluginsByNames.containsKey(name))
			return pluginsByNames.get(name);
		throw new IllegalArgumentException("has no plugin with name: " + name);
	}
	
	public EzyPlugin getPluginById(final Integer id) {
		if(pluginsByIds.containsKey(id))
			return pluginsByIds.get(id);
		throw new IllegalArgumentException("has no plugin with id: " + id);
	}
	
	public int getSize() {
		return plugins.size();
	}
	
}
