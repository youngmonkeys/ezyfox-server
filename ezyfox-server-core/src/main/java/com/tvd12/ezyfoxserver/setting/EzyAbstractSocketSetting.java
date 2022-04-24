package com.tvd12.ezyfoxserver.setting;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class EzyAbstractSocketSetting implements EzyBaseSocketSetting {

    @XmlElement(name = "port")
    protected int port;

    @XmlElement(name = "address")
    protected String address;

    @XmlElement(name = "active")
    protected boolean active;

    @XmlElement(name = "ssl-active")
    protected boolean sslActive;

    @XmlElement(name = "codec-creator")
    protected String codecCreator;

    public EzyAbstractSocketSetting() {
        setActive(true);
        setSslActive(false);
        setAddress("0.0.0.0");
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("port", port);
        map.put("address", address);
        map.put("active", active);
        map.put("codecCreator", codecCreator);
        return map;
    }
}
