package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleAppSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAppSetting setting = new EzySimpleAppSetting();
        setting.setMaxUsers(1);
        setting.setName("hello");
        setting.setHomePath("abc");
        System.out.println(setting.getLocation());
    }

}
