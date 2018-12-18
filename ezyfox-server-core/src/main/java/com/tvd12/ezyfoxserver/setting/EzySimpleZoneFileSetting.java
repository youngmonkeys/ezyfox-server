package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "zone")
public class EzySimpleZoneFileSetting {
    
    @XmlElement(name = "name")
    protected String name;
    
    @XmlElement(name = "config-file")
    protected String configFile;
    
    @XmlElement(name = "active")
    protected boolean active = true;
	
}
