package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventControllersImpl implements EzyEventControllers {

	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyEventController> controllers 
	        = new ConcurrentHashMap<>();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addController(EzyConstant eventType, EzyEventController controller) {
		controllers.put(eventType, controller);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public EzyEventController getController(EzyConstant eventType) {
		return controllers.get(eventType);
	}
	
	@Override
	public void destroy() {
	    if(controllers != null)
	        controllers.clear();
	    this.controllers = null;
	}
	
}
