package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserRequestHandler<C extends EzyContext, D> {
	
	D newData();
	
	void handle(C context, EzyUser user, D data);
	
}
