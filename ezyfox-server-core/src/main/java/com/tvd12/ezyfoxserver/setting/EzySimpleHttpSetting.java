package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "http")
public class EzySimpleHttpSetting implements EzyHttpSetting {

    @XmlElement(name = "port")
    protected int port = 8080;
    
    @XmlElement(name = "active")
    protected boolean active = true;
    
    @XmlElement(name = "max-threads")
    protected int maxThreads = 8;
    
}
