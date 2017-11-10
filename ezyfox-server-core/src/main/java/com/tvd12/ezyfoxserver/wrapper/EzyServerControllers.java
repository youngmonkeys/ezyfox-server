package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;

public interface EzyServerControllers {

	@SuppressWarnings("rawtypes")
	EzyController getController(EzyConstant cmd);
	
	@SuppressWarnings("rawtypes")
	EzyInterceptor getInterceptor(EzyConstant cmd);
}
