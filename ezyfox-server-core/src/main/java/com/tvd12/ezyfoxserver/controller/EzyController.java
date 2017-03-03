package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyController<T extends EzyData> {

	void handle(int appId, T data);
	
	Class<T> getDataType();
	
}
