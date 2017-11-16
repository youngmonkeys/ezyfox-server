package com.tvd12.ezyfoxserver.databridge.proxy;

import com.tvd12.ezyfoxserver.setting.EzyAdminSetting;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyProxyAdminUser {

	private final EzyAdminSetting setting;
	
	public static EzyProxyAdminUser proxyAdminUser(EzyAdminSetting setting) {
		return new EzyProxyAdminUser(setting);
	}
	
	public String getUsername() {
		return setting.getUsername();
	}
	
	public String getApiAccessToken() {
		return setting.getApiAccessToken();
	}
	
}
