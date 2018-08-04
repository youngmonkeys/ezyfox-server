package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzyEventControllers {

	@SuppressWarnings("rawtypes")
	EzyEventController getController(EzyConstant eventType);
	
	@SuppressWarnings("rawtypes")
	void addController(EzyConstant eventType, EzyEventController controller);
	
}
