package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

public interface EzyUserRequestHandler<C extends EzyContext, D> {
	
	void handle(C context, EzyUserSessionEvent event, D data);
	
	D newData();
	
}
