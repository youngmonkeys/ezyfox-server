package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.controller.EzyRequestAppController;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.interceptor.EzyServerUserInterceptor;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

public class EzyControllersImpl implements EzyControllers {

	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyController> controllers;
	
	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyInterceptor> interceptors;
	
	protected EzyControllersImpl(Builder builder) {
		this.controllers = builder.newControllers();
		this.interceptors = builder.newInterceptors();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public EzyController getController(EzyConstant cmd) {
		return controllers.get(cmd);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public EzyInterceptor getInterceptor(EzyConstant cmd) {
		return interceptors.get(cmd);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		@SuppressWarnings("rawtypes")
		protected Map<EzyConstant, EzyController> newControllers() {
			Map<EzyConstant, EzyController> answer = new ConcurrentHashMap<>();
			answer.put(EzyCommand.HAND_SHAKE, new EzyHandShakeController());
			answer.put(EzyCommand.LOGIN, new EzyLoginController());
			answer.put(EzyCommand.ACCESS_APP, new EzyAccessAppController());
			answer.put(EzyCommand.REQUEST_APP, new EzyRequestAppController());
			return answer;
		}
		
		@SuppressWarnings("rawtypes")
		protected Map<EzyConstant, EzyInterceptor> newInterceptors() {
			Map<EzyConstant, EzyInterceptor> answer = new ConcurrentHashMap<>();
			answer.put(EzyCommand.HAND_SHAKE, EzyInterceptor.EMPTY);
			answer.put(EzyCommand.LOGIN, EzyInterceptor.EMPTY);
			answer.put(EzyCommand.ACCESS_APP, new EzyServerUserInterceptor<>());
			answer.put(EzyCommand.REQUEST_APP, new EzyServerUserInterceptor<>());
			return answer;
		}
		
		public EzyControllers build() {
			return new EzyControllersImpl(this);
		}
	}

}
