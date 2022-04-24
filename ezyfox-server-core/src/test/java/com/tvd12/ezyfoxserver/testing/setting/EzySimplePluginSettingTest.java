package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting.EzySimpleListenEvents;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimplePluginSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePluginSetting setting = new EzySimplePluginSetting();
        setting.setZoneId(1);
        assert setting.getZoneId() == 1;
        setting.setHomePath("home");
        assert setting.getHomePath().equals("home");
        setting.setName("name");
        setting.setFolder("folder");
        assert setting.getFolder().equals("folder");
        setting.setFolder("");
        assert setting.getFolder().equals("name");
        System.out.println(setting.getLocation());
        System.out.println(setting.getConfigFile());
        assert setting.equals(setting);
        setting.setPriority(1);
        setting.setListenEvents(new EzySimpleListenEvents());
    }
}
