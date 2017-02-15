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
public class EzyFoxSettings {

	@XmlElement(name = "applications")
	private EzyFoxApps applications;
	
	@JsonIgnore
	public Set<String> getAppNames() {
		return applications.getAppNames();
	}
	
	@JsonIgnore
	public Set<Integer> getAppIds() {
		return applications.getAppIds();
	}
	
	public EzyFoxApp getAppByName(final String name) {
		return applications.getAppByName(name);
	}
	
	public EzyFoxApp getAppById(final Integer id) {
		return applications.getAppById(id);
	}
	
}
