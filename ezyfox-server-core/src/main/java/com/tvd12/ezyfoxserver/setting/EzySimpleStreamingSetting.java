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
@XmlRootElement(name = "streaming")
public class EzySimpleStreamingSetting implements EzyStreamingSetting {

    @XmlElement(name = "enable")
    private boolean enable;
    
}
