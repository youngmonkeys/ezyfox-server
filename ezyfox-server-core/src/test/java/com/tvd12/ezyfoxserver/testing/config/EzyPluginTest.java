package com.tvd12.ezyfoxserver.testing.config;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyPluginTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimplePluginSetting plugin = new EzySimplePluginSetting();
        plugin.setPriority(10);
    }
    
}
