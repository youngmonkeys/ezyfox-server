package com.tvd12.ezyfoxserver.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@ToString
@XmlRootElement(name = "settings")
public class EzyFoxSettings {

	@XmlElement(name = "applications")
	private EzyFoxApps applications;
	
}
