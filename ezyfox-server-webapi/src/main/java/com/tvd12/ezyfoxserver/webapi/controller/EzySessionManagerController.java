package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySession;
import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySessionDetails;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.webapi.exception.EzySessionNotFoundException;

@RestController
@RequestMapping("admin/sessions")
public class EzySessionManagerController extends EzyStatisticsController {

	@GetMapping("/count")
	public int getSessionCount() {
		return getServerSessionManger().getAllSessionCount();
	}
	
	@GetMapping("/all")
	public Collection<EzyProxySession> getSessions() {
		Collection<EzySession> sessions = getServerSessionManger().getAllSessions();
		return EzyProxySession.newCollection(sessions);
	}
	
	@GetMapping("/alive-count")
	public int getAliveSessionCount() {
		return getServerSessionManger().getAllSessionCount();
	}
	
	@GetMapping("/alive")
	public Collection<EzyProxySession> getAliveSessions() {
		Collection<EzySession> sessions = getServerSessionManger().getAllSessions();
		return EzyProxySession.newCollection(sessions);
	}
	
	@GetMapping("/{id}")
	public EzyProxySession getSession(@PathVariable long id) {
		EzySession session = getServerSessionManger().getSession(id);
		if(session == null)
			throw EzySessionNotFoundException.notFound(id);
		return EzyProxySessionDetails.proxySessionDetails(session);
	}
	
}
