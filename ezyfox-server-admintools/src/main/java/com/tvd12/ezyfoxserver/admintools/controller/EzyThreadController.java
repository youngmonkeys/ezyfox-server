package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.admintools.data.EzyThreadsDetail;
import com.tvd12.ezyfoxserver.admintools.service.EzyThreadsService;

@RestController
@RequestMapping("/admin/threads")
public class EzyThreadController {

	@Autowired
	private EzyThreadsService threadsService;

	@GetMapping("/count")
	public Long getThreadCount() {
		return threadsService.getThreadCount();
	}

	@GetMapping("/all")
	public EzyThreadsDetail getThreadsDetail() {
		EzyThreadsDetail ezyThreadsDetail = threadsService.getThreadsDetail();
		ezyThreadsDetail.sort();
		
		return ezyThreadsDetail;
	}

}
