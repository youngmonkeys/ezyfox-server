package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

public interface EzySettings {

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
