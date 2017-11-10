package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.EzyServer;

@RestController
@RequestMapping("admin/server-setting")
public class EzyServerSettingController extends EzyController {

	@GetMapping
	public EzyServer getSetverSetting() {
		return getServerContext().getServer(); 
	}
	
}
