package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyCpuPoint;
import com.tvd12.ezyfoxserver.monitor.EzyCpuMonitor;
import com.tvd12.ezyfoxserver.monitor.EzyMonitor;

@RestController
@RequestMapping("admin/cpu")
public class EzyCpuController {

	@Autowired
	protected EzyMonitor monitor;
	
	@GetMapping("/details")
	public EzyCpuPoint getDetails() {
		EzyCpuPoint point = new EzyCpuPoint();
		EzyCpuMonitor cpuMonitor = monitor.getCpuMonitor();
		point.setSystemCpuLoad(cpuMonitor.getSystemCpuLoad());
		point.setProcessCpuLoad(cpuMonitor.getProcessCpuLoad());
		return point;
	}
	
}
