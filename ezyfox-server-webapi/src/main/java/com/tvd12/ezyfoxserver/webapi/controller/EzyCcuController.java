package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/ccu")
public class EzyCcuController extends EzyAbstractController {

	@GetMapping
	public int getCcuCount() {
		return getServerSessionManger().getLoggedInSessionCount();
	}
	
}
