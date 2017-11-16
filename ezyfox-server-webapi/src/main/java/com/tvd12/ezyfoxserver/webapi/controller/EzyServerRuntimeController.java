package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyTime;

@RestController
@RequestMapping("admin/server")
public class EzyServerRuntimeController extends EzyStatisticsController {

	@GetMapping("/start-time")
	public EzyProxyTime getSetverSetting() {
		return new EzyProxyTime(getStatistics().getStartTime());
	}
	
}
