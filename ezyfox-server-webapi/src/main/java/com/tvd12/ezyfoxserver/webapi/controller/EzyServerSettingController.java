package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfoxserver.EzyServer;

public class EzyServerSettingController extends EzyAbstractController {

	public EzyServer getSetverSetting() {
		return getServerContext().getServer(); 
	}
	
	public String getServerVersion() {
		return getServer().getVersion();
	}
	
}
