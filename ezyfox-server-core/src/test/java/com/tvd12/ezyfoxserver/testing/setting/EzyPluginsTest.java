package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.test.base.BaseTest;

public class EzyPluginsTest extends BaseTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
        plugins.getPluginByName("zzz");
    }
    
}
