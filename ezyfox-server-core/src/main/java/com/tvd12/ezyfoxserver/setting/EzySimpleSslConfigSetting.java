package com.tvd12.ezyfoxserver.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ssl-config")
public class EzySimpleSslConfigSetting implements EzySslConfigSetting {

    @XmlElement(name = "file")
    protected String file
        = "ssl-config.properties";

    @XmlElement(name = "loader")
    protected String loader
        = "com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfigLoader";

    @XmlElement(name = "context-factory-builder")
    protected String contextFactoryBuilder
        = "com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactoryBuilder";

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("file", file);
        map.put("loader", loader);
        map.put("contextFactoryBuilder", contextFactoryBuilder);
        return map;
    }
}
