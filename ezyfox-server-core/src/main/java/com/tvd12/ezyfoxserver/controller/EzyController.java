package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.context.EzyContext;

public interface EzyController<R, D> {
	
	public void handle(EzyContext ctx, R rev, D data);
	
}
