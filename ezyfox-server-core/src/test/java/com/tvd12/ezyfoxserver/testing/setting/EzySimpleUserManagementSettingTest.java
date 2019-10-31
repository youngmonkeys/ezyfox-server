package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleUserManagementSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleUserManagementSetting setting = new EzySimpleUserManagementSetting();
        setting.setUserMaxIdleTime(1);
        setting.setUserMaxIdleTimeInSecond(1);
        setting.setMaxSessionPerUser(1);
        setting.setAllowGuestLogin(true);
        setting.setAllowChangeSession(true);
        setting.setGuestNamePrefix("");
        setting.setUserNamePattern("");
    }

}
