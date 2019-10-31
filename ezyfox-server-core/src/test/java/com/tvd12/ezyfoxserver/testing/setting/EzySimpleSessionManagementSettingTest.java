package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleEzyMaxRequestPerSecond;
import com.tvd12.test.base.BaseTest;

public class EzySimpleSessionManagementSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleSessionManagementSetting setting = new EzySimpleSessionManagementSetting();
        setting.setSessionMaxIdleTime(1);
        setting.setSessionMaxIdleTimeInSecond(1);
        setting.setSessionMaxRequestPerSecond(new EzySimpleEzyMaxRequestPerSecond());
        setting.setSessionMaxWaitingTime(1);
        setting.setSessionMaxWaitingTimeInSecond(1);
    }

}
