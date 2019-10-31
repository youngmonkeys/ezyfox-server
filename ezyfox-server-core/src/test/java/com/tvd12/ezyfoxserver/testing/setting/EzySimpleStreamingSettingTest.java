package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleStreamingSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleStreamingSetting setting = new EzySimpleStreamingSetting();
        setting.setEnable(true);
    }
    
}
