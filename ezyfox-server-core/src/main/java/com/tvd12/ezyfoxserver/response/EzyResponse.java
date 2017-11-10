package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyResponse {

	Object getData();
	
	EzyConstant getCommand();
	
	interface Builder extends EzyBuilder<EzyResponse> {
	}
}
