package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

@RestController
@RequestMapping("admin/network-stats")
public class EzyNetworkStatsController extends EzyController {

	@GetMapping("/total-read-bytes")
	public long getCcuCount() {
		EzyStatistics statistics = getServer().getStatistics();
		EzySocketStatistics socketStats = statistics.getSocketStats();
		EzyWebSocketStatistics webSocketStats = statistics.getWebSocketStats();
		return socketStats.getNetworkStats().getReadBytes() + webSocketStats.getNetworkStats().getReadBytes();
	}
	
}
