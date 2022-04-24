package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.test.assertion.Asserts;

public class EzySimpleZoneSettingTest {

    @Test
    public void test() {
        EzySimpleZoneSetting setting = new EzySimpleZoneSetting();
        setting.setConfigFile("config.properties");
        EzySimpleAppsSetting appsSetting = (EzySimpleAppsSetting)setting.getApplications();
        EzySimpleAppSetting appSetting = new EzySimpleAppSetting();
        appSetting.setName("app");
        appsSetting.setItem(appSetting);
        EzySimplePluginsSetting pluginsSetting = (EzySimplePluginsSetting)setting.getPlugins();
        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("plugin");
        pluginsSetting.setItem(pluginSetting);
        assert setting.getConfigFile().equals("config.properties");
        assert setting.getAppByName("app") == appSetting;
        assert setting.getPluginNames().size() == 1;
        assert setting.getPluginByName("plugin") == pluginSetting;
        assert setting.equals(setting);
        assert !setting.equals(new EzySimpleZoneSetting());
        assert setting.hashCode() == setting.hashCode();
        System.out.println(setting.toMap());
        System.out.println(setting);
        setting.setName("test");
        setting.setConfigFile("hello");
        setting.setMaxUsers(1);
        setting.setStreaming(new EzySimpleStreamingSetting());
        setting.setPlugins(new EzySimplePluginsSetting());
        setting.setApplications(new EzySimpleAppsSetting());
        setting.setUserManagement(new EzySimpleUserManagementSetting());
        setting.setEventControllers(new EzySimpleEventControllersSetting());
    }
    
    @Test
    public void configFileIsNull() {
        // given
        EzySimpleZoneSetting sut = new EzySimpleZoneSetting();

        // when
        String configFile = sut.getConfigFile();

        // then
        Asserts.assertNull(configFile);
        System.out.println(sut.toMap());
    }
}
