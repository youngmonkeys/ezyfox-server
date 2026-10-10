package com.tvd12.ezyfoxserver.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "protocol")
public class EzySimpleProtocolSetting implements EzyProtocolSetting {

    @XmlElement(name = "name")
    protected String name;

    @XmlElement(name = "active")
    protected boolean active = true;

    @XmlElement(name = "address")
    protected String address = "0.0.0.0";

    @XmlElement(name = "port")
    protected int port;

    @XmlElement(name = "ssl-active")
    protected boolean sslActive;

    @XmlElement(name = "max-request-size")
    protected int maxRequestSize = 4096;

    @XmlElement(name = "server-creator")
    protected String serverCreator;

    @XmlElement(name = "config-file")
    protected String configFile;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    protected List<EzySimpleProtocolPropertySetting> propertyList =
        new ArrayList<>();

    protected Map<String, String> properties = new HashMap<>();

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> answer = new HashMap<>();
        for (EzySimpleProtocolPropertySetting property : propertyList) {
            answer.put(property.getName(), property.getValue());
        }
        answer.putAll(properties);
        return answer;
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("active", active);
        map.put("address", address);
        map.put("port", port);
        map.put("sslActive", sslActive);
        map.put("maxRequestSize", maxRequestSize);
        map.put("serverCreator", serverCreator);
        map.put("configFile", configFile);
        map.put("properties", getProperties());
        return map;
    }
}
