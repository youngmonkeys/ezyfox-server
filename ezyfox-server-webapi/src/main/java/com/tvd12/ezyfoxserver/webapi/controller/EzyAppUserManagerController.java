package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.proxydata.EzyProxyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

@RestController
@RequestMapping("admin/users")
public class EzyAppUserManagerController extends EzyController {

	@GetMapping
	protected Collection<EzyProxyUser> getUsers(@RequestParam String appName) {
		List<EzyUser> users = getAppUserManager(appName).getUserList();
		return EzyProxyUser.newCollection(users);
	}
	
	protected EzyAppUserManager getAppUserManager(String appName) {
		return getAppContext(appName).getApp().getUserManager();
	}
	
}
