package com.tvd12.ezyfoxserver.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.ToString;

@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "applications")
public class EzyFoxApps {

	private final Map<Integer, EzyFoxApp> appsByIds;
	private final Map<String, EzyFoxApp> appsByNames;
	
	public EzyFoxApps() {
		this.appsByIds = new HashMap<>();
		this.appsByNames = new HashMap<>();
	}
	
	@XmlElement(name = "application")
	public void setItems(final EzyFoxApp[] items) {
		for(EzyFoxApp item : items)
			setItem(item);
	}
	
	@XmlTransient
	private void setItem(final EzyFoxApp item) {
		appsByIds.put(item.getId(), item);
		appsByNames.put(item.getName(), item);
	}
	
}
