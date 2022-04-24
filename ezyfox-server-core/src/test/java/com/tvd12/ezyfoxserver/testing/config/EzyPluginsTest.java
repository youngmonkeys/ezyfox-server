package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginsSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyPluginsTest extends BaseCoreTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
        plugins.getPluginById(-1);
    }

}
