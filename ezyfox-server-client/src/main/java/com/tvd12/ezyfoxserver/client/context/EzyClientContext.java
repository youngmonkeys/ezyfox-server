package com.tvd12.ezyfoxserver.client.context;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyClientContext 
		extends EzyPluginRequester, EzyContext, EzyDestroyable {

	EzyClient getClient();
	
	EzyClientUser getMe();
	
	void addAppContext(EzyClientAppContext context);
	
	EzyClientAppContext getAppContext(int appId);
	
	EzyClientAppContext getAppContext(String appName);
	
}
