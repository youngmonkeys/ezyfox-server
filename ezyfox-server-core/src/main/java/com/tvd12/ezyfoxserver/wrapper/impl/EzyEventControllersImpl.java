package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.setting.EzyEventControllerSetting;
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventControllersImpl implements EzyEventControllers {

	@SuppressWarnings("rawtypes")
	protected final Map<EzyConstant, EzyEventController> controllers 
	        = new ConcurrentHashMap<>();
	
	@SuppressWarnings("rawtypes")
    public static EzyEventControllers create(EzyEventControllersSetting setting) {
	    EzyEventControllers controllers = new EzyEventControllersImpl();
        for(EzyEventControllerSetting item : setting.getEventControllers()) {
            EzyEventType eventType = EzyEventType.valueOf(item.getEventType());
            EzyEventController controller = EzyClasses.newInstance(item.getController());
            controllers.addController(eventType, controller);
        }
        return controllers;
	}
	
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
	}
	
}
