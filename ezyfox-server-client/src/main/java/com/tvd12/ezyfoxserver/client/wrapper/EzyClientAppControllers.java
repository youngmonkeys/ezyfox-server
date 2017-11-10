package com.tvd12.ezyfoxserver.client.wrapper;

import com.tvd12.ezyfoxserver.client.controller.EzyClientAppController;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyClientAppControllers extends EzyDestroyable {
	
	@SuppressWarnings("rawtypes")
	EzyClientAppController getController(EzyConstant cmd);
	
	@SuppressWarnings("rawtypes")
	void addController(EzyConstant cmd, EzyClientAppController ctrl);
	
}
