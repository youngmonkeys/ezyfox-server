package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyController<T extends EzyData> {
	
	public void handle(EzyContext ctx, EzySession session, T data);
	
}
