package com.tvd12.ezyfoxserver.admintools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.admintools.data.EzyMemoryPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyMemoryService;

@RestController
@RequestMapping("/admin/memory")
public class EzyMemoryController {

	@Autowired
	private EzyMemoryService memoryService;

	@GetMapping("/details")
	public EzyMemoryPoint getMemoryDetails() {
		return memoryService.getMemoryDetails();
	}
}
