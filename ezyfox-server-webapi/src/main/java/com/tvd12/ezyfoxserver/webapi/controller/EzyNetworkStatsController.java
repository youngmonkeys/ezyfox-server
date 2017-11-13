package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.function.Function;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkRoStats;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

@RestController
@RequestMapping("admin/network-stats")
public class EzyNetworkStatsController extends EzyController {

	@GetMapping("/total-read-bytes")
	public long getTotalReadBytes() {
		return sumStatistics(stats -> stats.getReadBytes());
	}
	
	@GetMapping("/read-bytes-per-second")
	public long getReadBytesPerSecond() {
		return sumStatistics(stats -> stats.getReadBytesPerSecond());
	}
	
	@GetMapping("/read-bytes-per-minute")
	public long getReadBytesPerMinute() {
		return sumStatistics(stats -> stats.getReadBytesPerMinute());
	}
	
	protected long sumStatistics(Function<EzyNetworkRoStats, Long> function) {
		EzySocketStatistics socketStats = getSocketStatistics();
		EzyWebSocketStatistics webSocketStats = getWebSocketStatistics();
		EzyNetworkRoStats socketSetworkStats = socketStats.getNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = webSocketStats.getNetworkStats();
		long sum = function.apply(socketSetworkStats) + function.apply(webSocketNetworkStats);
		return sum;
	}
	
	protected EzyStatistics getStatistics() {
		return getServer().getStatistics();
	}
	
	protected EzySocketStatistics getSocketStatistics() {
		return getStatistics().getSocketStats();
	}
	
	protected EzyWebSocketStatistics getWebSocketStatistics() {
		return getStatistics().getWebSocketStats();
	}
}
