package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.admintools.data.EzyNetworkPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyNetWorkStatsService;

@RestController
@RequestMapping("/admin/network-stats")
public class EzyNetworkStatsController {

	@Autowired
	private EzyNetWorkStatsService networkStatsService;

	@GetMapping("/read-written-bytes-per-second")
	public EzyNetworkPoint getNetWorkStats() {
		return networkStatsService.getNetWorkStats();
	}

}
