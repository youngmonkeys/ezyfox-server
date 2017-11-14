package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.Collection;
import java.util.function.Function;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxySession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.statistics.EzySessionRoStats;

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
	
	protected long sumStatistics(Function<EzySessionRoStats, Long> function) {
		EzySessionRoStats socketSessionStats = getSocketSessionStats();
		EzySessionRoStats webSocketSessionStats = getWebSocketSessionStats();
		long sum = function.apply(socketSessionStats) + function.apply(webSocketSessionStats);
		return sum;
	}
	
	protected EzySessionRoStats getSocketSessionStats() {
		return getSocketStatistics().getSessionStats();
	}
	
	protected EzySessionRoStats getWebSocketSessionStats() {
		return getWebSocketStatistics().getSessionStats();
	}
	
}
