package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyArray;

public interface EzyUserRequestAppEvent extends EzyUserSessionEvent {
	
	EzyArray getData();
	
}
