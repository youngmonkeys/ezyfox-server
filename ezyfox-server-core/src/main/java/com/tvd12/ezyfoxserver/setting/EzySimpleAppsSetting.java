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
@XmlRootElement(name = "applications")
@JsonIgnoreProperties({"appsByIds", "appsByNames", "appNames", "appIds"})
@JsonPropertyOrder({"size", "apps"})
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
		if(appsByNames.containsKey(name))
			return appsByNames.get(name);
		throw new IllegalArgumentException("has no app with name: " + name);
	}
	
	@Override
	public EzySimpleAppSetting getAppById(Integer id) {
		if(appsByIds.containsKey(id))
			return appsByIds.get(id);
		throw new IllegalArgumentException("has no app with id: " + id);
	}
	
	@Override
	public int getSize() {
		return apps.size();
	}
	
}
