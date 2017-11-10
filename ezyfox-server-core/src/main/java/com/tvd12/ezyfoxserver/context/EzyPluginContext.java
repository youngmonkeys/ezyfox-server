package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyPlugin;

public interface EzyPluginContext extends EzyChildContext {
	
    EzyPlugin getPlugin();
	
}
