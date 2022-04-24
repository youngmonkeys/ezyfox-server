package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleAppsSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleAppsSetting setting = new EzySimpleAppsSetting();
        assert setting.getSize() == 0;
    }

}
