package com.tvd12.ezyfoxserver.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@XmlRootElement(name = "event-controllers")
public class EzySimpleEventControllersSetting implements EzyEventControllersSetting {

    private List<EzyEventControllerSetting> eventControllers = new ArrayList<>();
    
    @XmlElement(name = "event-controller")
    public void setItem(EzySimpleEventControllerSetting item) {
    	for(EzyEventControllerSetting existed : eventControllers) {
    		if(existed.getEventType().equals(item.getEventType()))
    			return;
    	}
    	eventControllers.add(item);
    }
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        List<Object> eventControllerMaps = new ArrayList<>();
        for(EzyEventControllerSetting eventController : eventControllers)
            eventControllerMaps.add(eventController.toMap());
        map.put("eventControllers", eventControllerMaps);
        return map;
    }
    
}
