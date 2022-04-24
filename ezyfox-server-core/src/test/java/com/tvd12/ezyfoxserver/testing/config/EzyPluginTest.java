package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyPluginTest extends BaseCoreTest {

    @Test
    public void test() {
        EzySimplePluginSetting plugin = new EzySimplePluginSetting();
        plugin.setPriority(10);
    }

}
