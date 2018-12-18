package com.tvd12.ezyfoxserver.setting;

import java.util.HashMap;
import java.util.Map;

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
@XmlRootElement(name = "event-controller")
public class EzySimpleEventHandlerSetting implements EzyEventControllerSetting {

    @XmlElement(name = "event-type")
    private String eventType;
    
    @XmlElement(name = "controller")
    private String controller;
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("eventType", eventType);
        map.put("controller", controller);
        return map;
    }
    
}
