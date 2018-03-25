package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;
import java.util.List;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzyAppUserManagerController extends EzyAbstractController {

	protected Collection<EzyProxyUser> getUsers(String appName) {
		List<EzyUser> users = getAppUserManager(appName).getUserList();
		return EzyProxyUser.newCollection(users);
	}
	
	protected EzyAppUserManager getAppUserManager(String appName) {
		return getAppContext(appName).getApp().getUserManager();
	}
	
}
