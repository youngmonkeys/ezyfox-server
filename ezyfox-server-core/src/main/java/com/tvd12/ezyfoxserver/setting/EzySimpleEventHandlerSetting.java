package com.tvd12.ezyfoxserver.setting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "event-controller")
@JsonPropertyOrder({"event-type", "controller"})
public class EzySimpleEventHandlerSetting implements EzyEventControllerSetting {

    @XmlElement(name = "event-type")
    private String eventType;
    
    @XmlElement(name = "controller")
    private String controller;
    
}
