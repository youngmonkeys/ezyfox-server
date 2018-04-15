package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyPlugin;

public interface EzyPluginContext extends EzyZoneChildContext {

    EzyPlugin getPlugin();
	
}
