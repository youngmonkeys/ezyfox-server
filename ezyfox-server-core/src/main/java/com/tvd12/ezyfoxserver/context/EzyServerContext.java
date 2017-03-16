package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyServer;

public interface EzyServerContext extends EzyContext {

	EzyServer getBoss();
	
	EzyAppContext getAppContext(int appId);
	
	EzyAppContext getAppContext(String appName);
	
	EzyPluginContext getPluginContext(int pluginId);
	
	EzyPluginContext getPluginContext(String pluginName);
	
}
