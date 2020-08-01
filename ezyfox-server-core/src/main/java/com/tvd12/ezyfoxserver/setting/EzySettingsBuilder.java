package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzySettingsBuilder implements EzyBuilder<EzySettings> {

    protected boolean debug;
    protected String nodeName = "ezyfox";
    protected int maxSessions = 999999;
    protected EzySimpleStreamingSetting streaming = new EzySimpleStreamingSetting();
    protected EzySimpleHttpSetting http = new EzySimpleHttpSetting();
    protected EzySimpleSocketSetting socket = new EzySimpleSocketSetting();
    protected EzySimpleUdpSetting udp = new EzySimpleUdpSetting();
    protected EzySimpleAdminsSetting admins = new EzySimpleAdminsSetting();
    protected EzySimpleLoggerSetting logger = new EzySimpleLoggerSetting();
    protected EzySimpleWebSocketSetting websocket = new EzySimpleWebSocketSetting();
    protected EzySimpleThreadPoolSizeSetting threadPoolSize = new EzySimpleThreadPoolSizeSetting();
    protected EzySimpleSessionManagementSetting sessionManagement = new EzySimpleSessionManagementSetting();
    protected EzySimpleEventControllersSetting eventControllers = new EzySimpleEventControllersSetting();
    protected EzySimpleZonesSetting zones = new EzySimpleZonesSetting();

    public EzySettingsBuilder debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public EzySettingsBuilder nodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    public EzySettingsBuilder maxSessions(int maxSessions) {
        this.maxSessions = maxSessions;
        return this;
    }

    public EzySettingsBuilder streaming(EzySimpleStreamingSetting streaming) {
        this.streaming = streaming;
        return this;
    }

    public EzySettingsBuilder http(EzySimpleHttpSetting http) {
        this.http = http;
        return this;
    }

    public EzySettingsBuilder socket(EzySimpleSocketSetting socket) {
        this.socket = socket;
        return this;
    }

    public EzySettingsBuilder udp(EzySimpleUdpSetting udp) {
        this.udp = udp;
        return this;
    }
    
    public EzySettingsBuilder admin(EzySimpleAdminSetting admin) {
        this.admins.setItem(admin);
        return this;
    }

    public EzySettingsBuilder admins(EzySimpleAdminsSetting admins) {
        this.admins = admins;
        return this;
    }

    public EzySettingsBuilder logger(EzySimpleLoggerSetting logger) {
        this.logger = logger;
        return this;
    }

    public EzySettingsBuilder websocket(EzySimpleWebSocketSetting websocket) {
        this.websocket = websocket;
        return this;
    }

    public EzySettingsBuilder threadPoolSize(EzySimpleThreadPoolSizeSetting threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public EzySettingsBuilder sessionManagement(EzySimpleSessionManagementSetting sessionManagement) {
        this.sessionManagement = sessionManagement;
        return this;
    }

    public EzySettingsBuilder eventControllers(EzySimpleEventControllersSetting eventControllers) {
        this.eventControllers = eventControllers;
        return this;
    }
    
    public EzySettingsBuilder zone(EzySimpleZoneSetting zone) {
        this.zones.setItem(zone);
        return this;
    }

    public EzySettingsBuilder zones(EzySimpleZonesSetting zones) {
        this.zones = zones;
        return this;
    }
    
    @Override
    public EzySimpleSettings build() {
        EzySimpleSettings p = new EzySimpleSettings();
        p.setDebug(debug);
        p.setNodeName(nodeName);
        p.setMaxSessions(maxSessions);
        p.setStreaming(streaming);
        p.setHttp(http);
        p.setSocket(socket);
        p.setUdp(udp);
        p.setAdmins(admins);
        p.setLogger(logger);
        p.setWebsocket(websocket);
        p.setThreadPoolSize(threadPoolSize);
        p.setSessionManagement(sessionManagement);
        p.setEventControllers(eventControllers);
        for(EzyZoneSetting zone : zones.getZones())
            p.addZone((EzySimpleZoneSetting) zone);
        return p;
    }
    
}
