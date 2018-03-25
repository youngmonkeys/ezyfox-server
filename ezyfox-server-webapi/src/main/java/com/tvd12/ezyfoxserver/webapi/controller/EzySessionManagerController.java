package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySession;
import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySessionDetails;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.webapi.exception.EzySessionNotFoundException;

public class EzySessionManagerController extends EzyStatisticsController {

	public int getSessionCount() {
		return getServerSessionManger().getAllSessionCount();
	}
	
	public Collection<EzyProxySession> getSessions() {
		Collection<EzySession> sessions = getServerSessionManger().getAllSessions();
		return EzyProxySession.newCollection(sessions);
	}
	
	public int getAliveSessionCount() {
		return getServerSessionManger().getAllSessionCount();
	}
	
	public Collection<EzyProxySession> getAliveSessions() {
		Collection<EzySession> sessions = getServerSessionManger().getAllSessions();
		return EzyProxySession.newCollection(sessions);
	}
	
	public EzyProxySession getSession(long id) {
		EzySession session = getServerSessionManger().getSession(id);
		if(session == null)
			throw EzySessionNotFoundException.notFound(id);
		return EzyProxySessionDetails.proxySessionDetails(session);
	}
	
}
