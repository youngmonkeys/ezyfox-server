package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyAppController extends EzyController {

	public void handle(EzyContext ctx, EzySession session, EzyArray data);
	
}
