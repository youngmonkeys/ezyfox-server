package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.event.EzyEvent;

public interface EzyFireEvent {

	void fire(EzyEventType type, EzyEvent event);
	
}
