package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySettingsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setNodeName("node");
        settings.setMaxSessions(100);
        settings.setStreaming(new EzySimpleStreamingSetting());
        settings.setHttp(new EzySimpleHttpSetting());
        settings.setSocket(new EzySimpleSocketSetting());
        settings.setAdmins(new EzySimpleAdminsSetting());
        settings.setLogger(new EzySimpleLoggerSetting());
        settings.setWebsocket(new EzySimpleWebSocketSetting());
        settings.setSessionManagement(new EzySimpleSessionManagementSetting());
        settings.setEventControllers(new EzySimpleEventControllersSetting());
        settings.setZoneFiles(new EzySimpleZoneFilesSetting());
        EzySimpleZonesSetting zonesSetting = settings.getZones();
        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        zoneSetting.setName("test");
        zonesSetting.setItem(zoneSetting);
        assert settings.getZoneByName("test") == zoneSetting;
        settings.setUdp(new EzySimpleUdpSetting());
        settings.setThreadPoolSize(new EzySimpleThreadPoolSizeSetting());
    }
}
