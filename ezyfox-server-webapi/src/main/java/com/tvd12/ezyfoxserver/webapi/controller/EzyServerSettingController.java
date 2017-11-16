package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.EzyServer;

@RestController
@RequestMapping("admin/server")
public class EzyServerSettingController extends EzyAbstractController {

	@GetMapping("/setting")
	public EzyServer getSetverSetting() {
		return getServerContext().getServer(); 
	}
	
	@GetMapping("/version")
	public String getServerVersion() {
		return getServer().getVersion();
	}
	
}
