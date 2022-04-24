package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleStreamingSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleStreamingSetting setting = new EzySimpleStreamingSetting();
        setting.setEnable(true);
    }

}
