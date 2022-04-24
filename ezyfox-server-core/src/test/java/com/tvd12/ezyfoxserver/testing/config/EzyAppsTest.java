package com.tvd12.ezyfoxserver.testing.config;

import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppsSetting;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyAppsTest extends BaseCoreTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test() {
        EzySimpleAppSetting app = new EzySimpleAppSetting();
        app.setName("app#1");
        EzySimpleAppsSetting apps = new EzySimpleAppsSetting();
        apps.setItem(app);
        apps.getAppById(-1);
    }

}
