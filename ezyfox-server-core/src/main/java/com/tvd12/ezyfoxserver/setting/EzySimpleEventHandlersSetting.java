package com.tvd12.ezyfoxserver.setting;

import java.util.ArrayList;
import java.util.List;

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
public class EzySimpleEventHandlersSetting implements EzyEventControllersSetting {

    private List<EzyEventControllerSetting> eventControllers = new ArrayList<>();
    
    @XmlElement(name = "event-controller")
    public void setItem(EzySimpleEventHandlerSetting item) {
        eventControllers.add(item);
    }
    
}
