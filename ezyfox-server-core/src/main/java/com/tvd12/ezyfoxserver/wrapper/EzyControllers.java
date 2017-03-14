package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyController;

public interface EzyControllers {

	@SuppressWarnings("rawtypes")
	EzyController getController(EzyConstant cmd);
	
}
