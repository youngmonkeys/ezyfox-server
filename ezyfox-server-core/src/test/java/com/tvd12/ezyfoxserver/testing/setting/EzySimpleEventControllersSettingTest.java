package com.tvd12.ezyfoxserver.testing.setting;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleEventControllersSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleEventControllersSetting setting = new EzySimpleEventControllersSetting();
        setting.setEventControllers(new ArrayList<>());
    }

}
