package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.Set;

public interface EzySettings extends EzyToMap {

    boolean isDebug();

    String getNodeName();

    int getMaxSessions();

    EzyHttpSetting getHttp();

    EzySocketSetting getSocket();

    EzyUdpSetting getUdp();

    EzyWebSocketSetting getWebsocket();

    EzyStreamingSetting getStreaming();

    EzyAdminsSetting getAdmins();

    EzyLoggerSetting getLogger();

    EzyThreadPoolSizeSetting getThreadPoolSize();

    EzySessionManagementSetting getSessionManagement();

    EzyZonesSetting getZones();

    Set<Integer> getZoneIds();

    Set<String> getZoneNames();

    EzyZoneSetting getZoneById(Integer id);

    EzyZoneSetting getZoneByName(String name);

    EzyEventControllersSetting getEventControllers();
}
