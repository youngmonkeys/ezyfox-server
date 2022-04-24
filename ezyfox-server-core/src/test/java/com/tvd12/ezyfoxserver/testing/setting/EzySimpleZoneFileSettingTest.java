package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleZoneFileSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleZoneFileSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleZoneFileSetting setting = new EzySimpleZoneFileSetting();
        setting.setName("name");
        setting.setConfigFile("file");
        setting.setActive(true);
    }
}
