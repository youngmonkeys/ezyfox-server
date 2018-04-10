package com.tvd12.ezyfoxserver.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "zones")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzySimpleZoneFilesSetting {
    
    @XmlElement(name = "zone")
    protected List<EzySimpleZoneFileSetting> zoneFiles = new ArrayList<>();
    
    public void forEach(Consumer<EzySimpleZoneFileSetting> consumer) {
        zoneFiles.forEach(consumer);
    }
	
}
