package com.tvd12.ezyfoxserver.testing.setting;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleAdminSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleAdminsSetting;
import com.tvd12.test.base.BaseTest;

public class EzySimpleAdminsSettingTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleAdminsSetting setting = new EzySimpleAdminsSetting();
        setting.setAdminsByName(new HashMap<>());
        setting.setAdminsByApiAccessToken(new HashMap<>());
        assert setting.getAdmins().size() == 0;
        EzySimpleAdminSetting adminSetting = new EzySimpleAdminSetting();
        adminSetting.setApiAccessToken("token");
        adminSetting.setUsername("user");
        adminSetting.setPassword("password");
        setting.setItem(adminSetting);
        assert setting.getAdminByName("user") != null;
        assert setting.getAdminByApiAccessToken("token") != null;
        assert setting.containsAdminByName("user");
        assert setting.containsAdminByApiAccessToken("token");
    }

}
