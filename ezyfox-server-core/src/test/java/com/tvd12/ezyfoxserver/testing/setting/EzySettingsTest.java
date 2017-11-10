package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.test.base.BaseTest;

public class EzySettingsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setWorkerPoolSize(1);
        settings.setSocket(new EzySimpleSocketSetting());
        settings.setPlugins(new EzySimplePluginsSetting());
        settings.setApplications(new EzySimpleAppsSetting());
        settings.setWebsocket(new EzySimpleWebSocketSetting());
    }
    
}
