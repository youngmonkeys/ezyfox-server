package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzyEventControllerAdder {

	@SuppressWarnings("rawtypes")
	void addEventController(EzyConstant eventType, EzyEventController ctrl);
	
}
