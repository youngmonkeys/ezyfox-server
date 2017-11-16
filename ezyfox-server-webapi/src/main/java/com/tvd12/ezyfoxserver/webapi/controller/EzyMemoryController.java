package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyMemoryPoint;
import com.tvd12.ezyfoxserver.monitor.EzyMemoryMonitor;
import com.tvd12.ezyfoxserver.monitor.EzyMonitor;

@RestController
@RequestMapping("admin/memory")
public class EzyMemoryController {

	@Autowired
	protected EzyMonitor monitor;
	
	@GetMapping("/details")
	public EzyMemoryPoint getDetails() {
		EzyMemoryPoint point = new EzyMemoryPoint();
		EzyMemoryMonitor memoryMonitor = monitor.getMemoryMonitor();
		point.setMaxMemory(memoryMonitor.getMaxMemory());
		point.setFreeMemory(memoryMonitor.getFreeMemory());
		point.setTotalMemory(memoryMonitor.getMaxMemory());
		return point;
	}
	
}
