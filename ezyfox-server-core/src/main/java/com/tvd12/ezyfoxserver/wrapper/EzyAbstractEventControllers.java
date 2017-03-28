package com.tvd12.ezyfoxserver.wrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.appcontroller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;

public abstract class EzyAbstractEventControllers implements EzyEventControllers {

	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyEventController> controllers;
	
	protected EzyAbstractEventControllers(Builder builder) {
		this.controllers = builder.newControllers();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addController(EzyConstant eventType, EzyEventController controller) {
		controllers.put(eventType, controller);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public EzyEventController getController(EzyConstant eventType) {
		if(controllers.containsKey(eventType))
			return controllers.get(eventType);
		throw new IllegalArgumentException("has no controller with event = " + eventType);
	}
	
	public abstract static class Builder {
		
		@SuppressWarnings("rawtypes")
		protected Map<EzyConstant, EzyEventController> newControllers() {
			Map<EzyConstant, EzyEventController> answer = new ConcurrentHashMap<>();
			addControllers(answer);
			return answer;
		}
		
		@SuppressWarnings("rawtypes")
		protected void addControllers(Map<EzyConstant, EzyEventController> answer) {
			answer.put(EzyEventType.SERVER_READY, new EzyServerReadyController());
		}
		
		public abstract EzyEventControllers build();
	}
	
}