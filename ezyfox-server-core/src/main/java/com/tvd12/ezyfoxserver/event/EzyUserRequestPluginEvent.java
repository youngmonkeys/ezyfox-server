package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyUserRequestPluginEvent extends EzyUserSessionEvent {
	
	EzyArray getData();
	
}
