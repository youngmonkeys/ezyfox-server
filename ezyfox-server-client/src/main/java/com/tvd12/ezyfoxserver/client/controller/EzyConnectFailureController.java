package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyConnectFailureController {

	EzyConnectFailureController EMPTY = (error) -> {};
	
	void handle(EzyConstant error);
	
}
