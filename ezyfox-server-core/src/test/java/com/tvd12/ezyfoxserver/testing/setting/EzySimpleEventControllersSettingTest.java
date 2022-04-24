package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class EzySimpleEventControllersSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleEventControllersSetting setting = new EzySimpleEventControllersSetting();
        setting.setEventControllers(new ArrayList<>());
    }

}
