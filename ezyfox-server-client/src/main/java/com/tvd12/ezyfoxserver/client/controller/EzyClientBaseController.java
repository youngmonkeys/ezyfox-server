package com.tvd12.ezyfoxserver.client.controller;

public interface EzyClientBaseController<C, R, D> {
	
	public void handle(C ctx, R rev, D data);
	
}
