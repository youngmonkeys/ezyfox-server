package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimplePluginsSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePluginsSetting setting = new EzySimplePluginsSetting();
        assert setting.getSize() == 0;
    }
    
}
