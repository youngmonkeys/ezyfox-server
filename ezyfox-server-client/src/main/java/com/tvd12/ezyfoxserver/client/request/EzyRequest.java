package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyRequest {

	int getAppId();
	
	Object getData();
	
	EzyConstant getCommand();
	
}
