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
@XmlRootElement(name = "udp")
public class EzySimpleUdpSetting implements EzyUdpSetting {

    @XmlElement(name = "port")
    protected int port;

    @XmlElement(name = "address")
    protected String address;

    @XmlElement(name = "active")
    protected boolean active;

    @XmlElement(name = "max-request-size")
    protected int maxRequestSize;

    @XmlElement(name = "channel-pool-size")
    protected int channelPoolSize;

    @XmlElement(name = "handler-thread-pool-size")
    protected int handlerThreadPoolSize;

    public EzySimpleUdpSetting() {
        super();
        setPort(2611);
        setAddress("0.0.0.0");
        setActive(false);
        setMaxRequestSize(1024);
        setChannelPoolSize(16);
        setHandlerThreadPoolSize(5);
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("port", port);
        map.put("address", address);
        map.put("active", active);
        map.put("maxRequestSize", maxRequestSize);
        map.put("channelPoolSize", channelPoolSize);
        map.put("handlerThreadPoolSize", handlerThreadPoolSize);
        return map;
    }
}
