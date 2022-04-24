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

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "thread-pool-size")
public class EzySimpleThreadPoolSizeSetting
    implements EzyThreadPoolSizeSetting {

    @XmlElement(name = "statistics")
    protected int statistics = 1;

    @XmlElement(name = "stream-handler")
    protected int streamHandler = 8;

    @XmlElement(name = "socket-data-receiver")
    protected int socketDataReceiver = 8;

    @XmlElement(name = "system-request-handler")
    protected int systemRequestHandler = 8;

    @XmlElement(name = "extension-request-handler")
    protected int extensionRequestHandler = 8;

    @XmlElement(name = "socket-disconnection-handler")
    protected int socketDisconnectionHandler = 2;

    @XmlElement(name = "socket-user-removal-handler")
    protected int socketUserRemovalHandler = 3;

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("statistics", statistics);
        map.put("streamHandler", streamHandler);
        map.put("socketDataReceiver", socketDataReceiver);
        map.put("systemRequestHandler", systemRequestHandler);
        map.put("extensionRequestHandler", extensionRequestHandler);
        map.put("socketDisconnectionHandler", socketDisconnectionHandler);
        map.put("socketUserRemovalHandler", socketUserRemovalHandler);
        return map;
    }
}
