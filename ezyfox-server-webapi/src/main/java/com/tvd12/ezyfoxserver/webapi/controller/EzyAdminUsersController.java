package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyAdminUser;
import com.tvd12.ezyfoxserver.setting.EzyAdminSetting;
import com.tvd12.ezyfoxserver.setting.EzyAdminsSetting;
import com.tvd12.ezyfoxserver.webapi.exception.EzyUserNotFoundException;

@RestController
@RequestMapping("admin/users")
public class EzyAdminUsersController extends EzyAbstractController {

	@PostMapping("/get")
	public EzyProxyAdminUser getAdminUser(@RequestBody Credential credential) {
		EzyAdminsSetting admins = getAdmins();
		String username = credential.getUsername();
		String password = credential.getPassword();
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
