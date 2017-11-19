package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.admintools.data.EzyTrafficDetails;
import com.tvd12.ezyfoxserver.admintools.service.EzyTrafficService;

@RestController
@RequestMapping("/admin/traffic")
public class EzyTrafficController {
	
	@Autowired
	private EzyTrafficService trafficService;
	
	@GetMapping("/details")
	public EzyTrafficDetails getTrafficDetails() {
		return trafficService.getTrafficDetails();
	}
}
