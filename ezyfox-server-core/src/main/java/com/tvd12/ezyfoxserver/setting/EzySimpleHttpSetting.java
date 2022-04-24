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
@XmlRootElement(name = "http")
public class EzySimpleHttpSetting implements EzyHttpSetting {

    @XmlElement(name = "port")
    protected int port = 8080;

    @XmlElement(name = "active")
    protected boolean active = false;

    @XmlElement(name = "max-threads")
    protected int maxThreads = 8;

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("port", port);
        map.put("active", active);
        map.put("maxThreads", maxThreads);
        return map;
    }
}
