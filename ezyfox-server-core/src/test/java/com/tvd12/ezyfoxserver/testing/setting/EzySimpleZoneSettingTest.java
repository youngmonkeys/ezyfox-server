package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;

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
        assert setting.hashCode() == setting.hashCode();
        System.out.println(setting.toMap());
        System.out.println(setting);
    }
    
}
