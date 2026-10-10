package com.tvd12.ezyfoxserver.setting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "protocols")
public class EzySimpleProtocolsSetting implements EzyProtocolsSetting {

    @XmlElement(name = "writer-thread-pool-size")
    protected int writerThreadPoolSize = 8;

    protected List<EzyProtocolSetting> protocols = new ArrayList<>();

    @XmlElement(name = "protocol")
    public void setItem(EzySimpleProtocolSetting item) {
        protocols.add(item);
    }

    @Override
    public List<EzyProtocolSetting> getActiveProtocols() {
        return protocols
            .stream()
            .filter(EzyProtocolSetting::isActive)
            .collect(Collectors.toList());
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("writerThreadPoolSize", writerThreadPoolSize);
        map.put(
            "protocols",
            protocols
                .stream()
                .map(EzyProtocolSetting::toMap)
                .collect(Collectors.toList())
        );
        return map;
    }
}
