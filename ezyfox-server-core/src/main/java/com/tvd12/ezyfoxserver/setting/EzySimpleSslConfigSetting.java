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
@XmlRootElement(name = "ssl-config")
public class EzySimpleSslConfigSetting implements EzySslConfigSetting {

    @XmlElement(name = "file")
    protected String file = "ssl-config.properties";
    
    @XmlElement(name = "loader")
    protected String loader = "com.tvd12.ezyfoxserver.mapping.ssl.EzySimpleSslConfigLoader";
    
    @XmlElement(name = "context-factory-builder")
    protected String contextFactoryBuilder = "com.tvd12.ezyfoxserver.mapping.ssl.EzySimpleSslContextFactoryBuilder";
    
}
