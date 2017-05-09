package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.proxydata.EzyProxySession;

@RestController
@RequestMapping("admin/sessions")
public class EzySessionManagerController extends EzyController {

	@GetMapping("/all")
	public Collection<EzyProxySession> getSessions() {
		Collection<EzySession> sessions = getServerSessionManger().getAllSessions();
		return EzyProxySession.newCollection(sessions);
	}
	
}
