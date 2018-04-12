package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;
import java.util.List;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public class EzyAppUserManagerController extends EzyAbstractController {

	protected Collection<EzyProxyUser> getUsers(String zoneName, String appName) {
		EzyAppUserManager userManager = getAppUserManager(zoneName, appName);
		List<EzyUser> users = userManager.getUserList();
		return EzyProxyUser.newCollection(users);
	}
	
	protected EzyAppUserManager getAppUserManager(String zoneName, String appName) {
		EzyZoneContext zoneContext = serverContext.getZoneContext(zoneName);
		EzyAppContext appContext = zoneContext.getAppContext(appName);
		return appContext.getApp().getUserManager();
	}
	
}
