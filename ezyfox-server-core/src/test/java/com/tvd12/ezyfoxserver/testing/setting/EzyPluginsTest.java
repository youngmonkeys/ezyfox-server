package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyPluginsTest extends BaseTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
        plugins.getPluginByName("zzz");
    }

}
