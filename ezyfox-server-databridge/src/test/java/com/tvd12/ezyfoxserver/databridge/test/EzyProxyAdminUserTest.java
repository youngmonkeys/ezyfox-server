package com.tvd12.ezyfoxserver.databridge.test;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyAdminUser;
import com.tvd12.ezyfoxserver.setting.EzySimpleAdminSetting;

public class EzyProxyAdminUserTest {

	@Test
	public void test() {
		EzySimpleAdminSetting setting = new EzySimpleAdminSetting();
		setting.setUsername("name");
		setting.setApiAccessToken("accessToken");
		EzyProxyAdminUser proxy = EzyProxyAdminUser.proxyAdminUser(setting);
		assert proxy.getUsername().equals("name");
		assert proxy.getApiAccessToken().equals("accessToken");
	}
	
}
