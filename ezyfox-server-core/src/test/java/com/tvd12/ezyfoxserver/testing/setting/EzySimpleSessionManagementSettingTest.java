package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleSessionManagementSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSessionManagementSetting setting = new EzySimpleSessionManagementSetting();
        setting.setSessionMaxIdleTime(1);
        setting.setSessionMaxIdleTimeInSecond(1);
        setting.setSessionMaxRequestPerSecond(new EzySimpleMaxRequestPerSecond());
        setting.setSessionMaxWaitingTime(1);
        setting.setSessionMaxWaitingTimeInSecond(1);
    }
}
