package com.tvd12.ezyfoxserver.testing.config;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyPluginsTest extends BaseCoreTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
        plugins.getPluginById(-1);
    }
    
}
