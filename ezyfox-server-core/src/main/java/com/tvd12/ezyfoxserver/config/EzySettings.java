package com.tvd12.ezyfoxserver.config;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "settings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzySettings {

	@XmlElement(name = "applications")
	private EzyApps applications;
	
	@XmlElement(name = "plugins")
	private EzyPlugins plugins;
	
	//==================== apps ================//
	@JsonIgnore
	public Set<String> getAppNames() {
		return applications.getAppNames();
	}
	
	@JsonIgnore
	public Set<Integer> getAppIds() {
		return applications.getAppIds();
	}
	
	public EzyApp getAppByName(final String name) {
		return applications.getAppByName(name);
	}
	
	public EzyApp getAppById(final Integer id) {
		return applications.getAppById(id);
	}
	//=============================================//
	
	//==================== plugins ================//
	@JsonIgnore
	public Set<String> getPluginNames() {
		return plugins.getPluginNames();
	}
	
	@JsonIgnore
	public Set<Integer> getPluginIds() {
		return plugins.getPluginIds();
	}
	
	public EzyPlugin getPluginByName(final String name) {
		return plugins.getPluginByName(name);
	}
	
	public EzyPlugin getPluginById(final Integer id) {
		return plugins.getPluginById(id);
	}
	//=============================================//
	
}
