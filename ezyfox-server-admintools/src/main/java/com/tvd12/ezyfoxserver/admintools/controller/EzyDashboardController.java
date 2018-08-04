package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tvd12.ezyfox.util.EzyLoggable;

@Controller
@RequestMapping("/")
public class EzyDashboardController extends EzyLoggable  {
	
	public EzyDashboardController() {
		getLogger().debug("\ndashboard controller init\n");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String handleRequest() {
		return "dashboard";
	}
}
