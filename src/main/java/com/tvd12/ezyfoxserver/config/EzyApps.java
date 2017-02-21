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
@XmlRootElement(name = "applications")
@JsonIgnoreProperties({"appsByIds", "appsByNames", "appNames", "appIds"})
@JsonPropertyOrder({"size", "apps"})
public class EzyApps {

	private final List<EzyApp> apps;
	private final Map<Integer, EzyApp> appsByIds;
	private final Map<String, EzyApp> appsByNames;
	
	public EzyApps() {
		this.apps = new ArrayList<>();
		this.appsByIds = new HashMap<>();
		this.appsByNames = new HashMap<>();
	}
	
	@XmlElement(name = "application")
	public void setItems(final EzyApp[] items) {
		for(EzyApp item : items)
			setItem(item);
	}
	
	@XmlTransient
	private void setItem(final EzyApp item) {
		apps.add(item);
		appsByIds.put(item.getId(), item);
		appsByNames.put(item.getName(), item);
	}
	
	public Set<String> getAppNames() {
		return appsByNames.keySet();
	}
	
	public Set<Integer> getAppIds() {
		return appsByIds.keySet();
	}
	
	public EzyApp getAppByName(final String name) {
		if(appsByNames.containsKey(name))
			return appsByNames.get(name);
		throw new IllegalArgumentException("has no app with name: " + name);
	}
	
	public EzyApp getAppById(final Integer id) {
		if(appsByIds.containsKey(id))
			return appsByIds.get(id);
		throw new IllegalArgumentException("has no app with id: " + id);
	}
	
	public int getSize() {
		return apps.size();
	}
	
}
