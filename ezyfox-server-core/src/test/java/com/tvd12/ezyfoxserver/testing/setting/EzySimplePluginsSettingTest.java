package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimplePluginsSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimplePluginsSetting setting = new EzySimplePluginsSetting();
        assert setting.getSize() == 0;
    }
}
