package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyAppsTest extends BaseTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimpleAppsSetting apps = new EzySimpleAppsSetting();
        apps.getAppByName("zzz");
    }
}
