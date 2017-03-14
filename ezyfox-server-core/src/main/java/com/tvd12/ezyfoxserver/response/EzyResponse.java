package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyResponse {

	int getAppId();
	
	Object getData();
	
	EzyConstant getCommand();
	
}
