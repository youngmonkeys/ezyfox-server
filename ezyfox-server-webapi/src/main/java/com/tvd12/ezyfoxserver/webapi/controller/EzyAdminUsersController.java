package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyAdminUser;
import com.tvd12.ezyfoxserver.setting.EzyAdminSetting;
import com.tvd12.ezyfoxserver.setting.EzyAdminsSetting;
import com.tvd12.ezyfoxserver.webapi.exception.EzyUserNotFoundException;

public class EzyAdminUsersController extends EzyAbstractController {

	public EzyProxyAdminUser getAdminUser(String username, String password) {
		EzyAdminsSetting admins = getAdmins();
		EzyAdminSetting setting = admins.getAdminByName(username);
		if(setting == null) 
			throw EzyUserNotFoundException.notFound(username);
		if(!setting.getPassword().equals(password))
			throw EzyUserNotFoundException.invalidPassword(username);
		return EzyProxyAdminUser.proxyAdminUser(setting);
	}
	
	protected EzyAdminsSetting getAdmins() {
		return getSettings().getAdmins();
	}
}
