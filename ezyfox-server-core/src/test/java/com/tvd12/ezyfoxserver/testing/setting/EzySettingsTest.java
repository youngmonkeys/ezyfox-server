package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAdminsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneFilesSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZonesSetting;
import com.tvd12.test.base.BaseTest;

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
    }
    
}
