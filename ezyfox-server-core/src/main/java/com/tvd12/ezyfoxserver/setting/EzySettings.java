package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzySettings extends EzyToMap {

    boolean isDebug();
    
    String getNodeName();
    
    int getMaxSessions();
    
    EzyHttpSetting getHttp();
    
    EzySocketSetting getSocket();
    
    EzyWebSocketSetting getWebsocket();
    
    EzyStreamingSetting getStreaming();
    
    EzyAdminsSetting getAdmins();
    
    EzyLoggerSetting getLogger();
    
    EzySessionManagementSetting getSessionManagement();
    
    EzyZonesSetting getZones();
    
    Set<Integer> getZoneIds();

    Set<String> getZoneNames();

    EzyZoneSetting getZoneById(Integer id);

    EzyZoneSetting getZoneByName(String name);
    
    EzyEventControllersSetting getEventControllers();
}
