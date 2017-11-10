package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class EzyAbstractSocketSetting implements EzyBaseSocketSetting {

    @XmlElement(name = "port")
    protected int port;
    
    @XmlElement(name = "address")
    protected String address;
    
	@XmlElement(name = "codec-creator")
	protected String codecCreator;
	
	public EzyAbstractSocketSetting() {
	    setAddress("0.0.0.0");
	}
	
}
