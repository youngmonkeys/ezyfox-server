package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.admintools.data.EzyCpuPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyCPUService;

@RestController
@RequestMapping("/admin/cpu")
public class EzyCPUController {
	
	@Autowired
	private EzyCPUService cpuService;
	
	@GetMapping("/details")
	private EzyCpuPoint getCPUDetails() {
		return cpuService.getCPUDetails();
	}
	

}
