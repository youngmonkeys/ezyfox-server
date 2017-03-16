package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.config.EzyPlugin;

public interface EzyPluginContext extends EzyContext {
	
	EzyPlugin getPlugin();
	
}
