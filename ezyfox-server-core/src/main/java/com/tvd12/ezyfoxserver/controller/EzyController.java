package com.tvd12.ezyfoxserver.controller;

public interface EzyController<C, R, D> {
	
	void handle(C ctx, R rev, D data);
	
}
