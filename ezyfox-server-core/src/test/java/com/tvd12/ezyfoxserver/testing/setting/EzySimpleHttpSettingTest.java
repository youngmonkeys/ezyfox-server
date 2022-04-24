package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleHttpSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleHttpSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleHttpSetting setting = new EzySimpleHttpSetting();
        setting.setActive(true);
        setting.setMaxThreads(1);
        setting.setPort(8081);
        System.out.println(setting.toMap());
    }
}
