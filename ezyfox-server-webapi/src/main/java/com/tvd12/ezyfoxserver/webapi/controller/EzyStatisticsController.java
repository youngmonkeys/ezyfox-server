package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

public class EzyStatisticsController extends EzyAbstractController {

	protected final EzyStatistics getStatistics() {
		return getServer().getStatistics();
	}
	
	protected final EzySocketStatistics getSocketStatistics() {
		return getStatistics().getSocketStats();
	}
	
	protected final EzyWebSocketStatistics getWebSocketStatistics() {
		return getStatistics().getWebSocketStats();
	}
	
}
