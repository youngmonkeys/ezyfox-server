package com.tvd12.ezyfoxserver.context;

import java.util.function.Predicate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;

public interface EzyZoneContext extends EzyComplexContext {

	EzyZone getZone();
	
	EzyServerContext getParent();
	
	EzyAppContext getAppContext(String appName);
	
	EzyPluginContext getPluginContext(String pluginName);
	
	void firePluginEvent(EzyConstant type, EzyEvent event);
	
	void fireAppEvent(EzyConstant type, EzyEvent event);
	
	void fireAppEvent(EzyConstant type, EzyEvent event, String username);
	
	void fireAppEvent(EzyConstant type, EzyEvent event, EzyUser user);
	
	void fireAppEvent(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter);
	
}
