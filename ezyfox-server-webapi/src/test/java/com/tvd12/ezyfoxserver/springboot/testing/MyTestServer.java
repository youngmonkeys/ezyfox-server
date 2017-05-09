package com.tvd12.ezyfoxserver.springboot.testing;

import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;

public class MyTestServer extends EzySimpleServer {

	public MyTestServer() {
		EzySimpleSettings settings = new EzySimpleSettings();
		setSettings(settings);
	}
	
}
