package com.tvd12.ezyfoxserver.wrapper;

import java.util.List;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public interface EzyEventControllers extends EzyDestroyable {

	@SuppressWarnings("rawtypes")
	List<EzyEventController> getControllers(EzyConstant eventType);
	
	@SuppressWarnings("rawtypes")
	void addController(EzyConstant eventType, EzyEventController controller);
	
}
