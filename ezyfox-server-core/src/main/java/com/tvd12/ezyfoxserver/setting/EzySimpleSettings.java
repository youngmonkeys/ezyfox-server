package com.tvd12.ezyfoxserver.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "settings")
public class EzySimpleSettings implements EzySettings {

    @XmlElement(name = "debug")
    protected boolean debug;
    
    @XmlElement(name = "node-name")
    protected String nodeName = "ezyfox";
    
    @XmlElement(name = "max-sessions")
    protected int maxSessions = 999999;
    
    @XmlElement(name = "streaming")
    protected EzySimpleStreamingSetting streaming = new EzySimpleStreamingSetting();
    
    @XmlElement(name = "http")
    protected EzySimpleHttpSetting http = new EzySimpleHttpSetting();

    @XmlElement(name = "socket")
    protected EzySimpleSocketSetting socket = new EzySimpleSocketSetting();

    @XmlElement(name = "udp")
    protected EzySimpleUdpSetting udp = new EzySimpleUdpSetting();

    @XmlElement(name = "administrators")
    protected EzySimpleAdminsSetting admins = new EzySimpleAdminsSetting();

    @XmlElement(name = "logger")
    protected EzySimpleLoggerSetting logger = new EzySimpleLoggerSetting();

    @XmlElement(name = "web-socket")
    protected EzySimpleWebSocketSetting websocket = new EzySimpleWebSocketSetting();

    @XmlElement(name = "thread-pool-size")
    protected EzySimpleThreadPoolSizeSetting threadPoolSize = new EzySimpleThreadPoolSizeSetting();

    @XmlElement(name = "session-management")
    protected EzySimpleSessionManagementSetting sessionManagement = new EzySimpleSessionManagementSetting();

    @XmlElement(name = "event-controllers")
    protected EzySimpleEventControllersSetting eventControllers = new EzySimpleEventControllersSetting();

    @Setter(AccessLevel.NONE)
    protected EzySimpleZonesSetting zones = new EzySimpleZonesSetting();
    
    @XmlElement(name = "zones")
    protected EzySimpleZoneFilesSetting zoneFiles = new EzySimpleZoneFilesSetting();
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("debug", debug);
        map.put("nodeName", nodeName);
        map.put("maxSessions", maxSessions);
        map.put("threadPoolSize", threadPoolSize.toMap());
        map.put("streaming", streaming.toMap());
        map.put("socket", socket.toMap());
        map.put("udp", udp.toMap());
        map.put("websocket", websocket.toMap());
        map.put("logger", logger.toMap());
        map.put("admins", admins.toMap());
        map.put("sessionManagement", sessionManagement.toMap());
        map.put("eventControllers", eventControllers.toMap());
        map.put("zones", zones.toMap());
        return map;
    }

    //==================== apps ================//
    public void addZone(EzySimpleZoneSetting zone) {
        this.zones.setItem(zone);
    }
    
    @Override
    public Set<String> getZoneNames() {
        return zones.getZoneNames();
    }

    @Override
    public Set<Integer> getZoneIds() {
        return zones.getZoneIds();
    }

    @Override
    public EzySimpleZoneSetting getZoneByName(String name) {
        return zones.getZoneByName(name);
    }

    @Override
    public EzySimpleZoneSetting getZoneById(Integer id) {
        return zones.getZoneById(id);
    }
    //=============================================//

}
