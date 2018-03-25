package com.tvd12.ezyfoxserver.testing.config;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySettingsTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimpleSettings settings = new EzySimpleSettings();
        settings.setThreadPoolSize(100);
        settings.setApplications(new EzySimpleAppsSetting());
        settings.setPlugins(new EzySimplePluginsSetting());
    }
    
}
