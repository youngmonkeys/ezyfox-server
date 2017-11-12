package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.monitor.EzyMonitor;
import com.tvd12.ezyfoxserver.monitor.EzyThreadsMonitor;
import com.tvd12.ezyfoxserver.monitor.data.EzyThreadsDetail;

@RestController
@RequestMapping("admin/threads")
public class EzyThreadsController extends EzyController {

	@Autowired
	protected EzyMonitor monitor;
	
	@GetMapping("/all")
	public EzyThreadsDetail getThreads() {
		return getThreadsMonitor().getThreadsDetails();
	}
	
	protected EzyThreadsMonitor getThreadsMonitor() {
		return monitor.getThreadsMonitor();
	}
	
}
