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
@XmlRootElement(name = "streaming")
public class EzySimpleStreamingSetting implements EzyStreamingSetting {

    @XmlElement(name = "enable")
    private boolean enable;

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("enable", enable);
        return map;
    }
}
