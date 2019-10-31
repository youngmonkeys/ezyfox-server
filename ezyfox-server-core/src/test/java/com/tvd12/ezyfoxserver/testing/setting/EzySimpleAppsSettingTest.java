package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleAppsSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAppsSetting setting = new EzySimpleAppsSetting();
        assert setting.getSize() == 0;
    }

}
