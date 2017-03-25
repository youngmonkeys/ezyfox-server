package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.event.EzyEvent;

public interface EzyFireEvent {

	void fire(EzyConstant type, EzyEvent event);
	
}
