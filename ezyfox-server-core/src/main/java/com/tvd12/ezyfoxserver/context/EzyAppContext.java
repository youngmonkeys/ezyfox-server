package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.config.EzyApp;

public interface EzyAppContext extends EzyContext {

	EzyApp getApp();
	
}
