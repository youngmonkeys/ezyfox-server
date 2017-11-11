package com.tvd12.ezyfoxserver.client.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.controller.EzyClientAppController;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientAppControllers;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyClientAppControllersImpl implements EzyClientAppControllers {
	
	@SuppressWarnings("rawtypes") 
	protected Map<EzyConstant, EzyClientAppController> controllers = new ConcurrentHashMap<>();
	
	protected EzyClientAppControllersImpl(Builder builder) {
	}
	
	@SuppressWarnings("rawtypes") 
	@Override 
	public EzyClientAppController getController(EzyConstant cmd) {
		return controllers.get(cmd);
	} 
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addController(EzyConstant cmd, EzyClientAppController ctrl) {
		controllers.put(cmd, ctrl);
	}
	
	@Override
	public void destroy() {
		controllers.clear();
		controllers = null;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements EzyBuilder<EzyClientAppControllers> {
		
		@Override
		public EzyClientAppControllers build() {
			return new EzyClientAppControllersImpl(this);
		}
	}
	
}
