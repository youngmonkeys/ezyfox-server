package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyHandshakeController;
import com.tvd12.ezyfoxserver.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.controller.EzyPingController;
import com.tvd12.ezyfoxserver.controller.EzyPluginInfoController;
import com.tvd12.ezyfoxserver.controller.EzyRequestAppController;
import com.tvd12.ezyfoxserver.controller.EzyRequestPluginByIdController;
import com.tvd12.ezyfoxserver.controller.EzyRequestPluginByNameController;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.interceptor.EzyServerUserInterceptor;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;

import lombok.Setter;

@Setter
public class EzyServerControllersImpl implements EzyServerControllers {

	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyController> controllers;
	
	@SuppressWarnings("rawtypes")
	protected Map<EzyConstant, EzyInterceptor> interceptors;
	
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
			answer.put(EzyCommand.PING, new EzyPingController());
			answer.put(EzyCommand.HANDSHAKE, new EzyHandshakeController());
			answer.put(EzyCommand.LOGIN, new EzyLoginController());
			answer.put(EzyCommand.APP_ACCESS, new EzyAccessAppController());
			answer.put(EzyCommand.APP_REQUEST, new EzyRequestAppController());
			answer.put(EzyCommand.PLUGIN_INFO, new EzyPluginInfoController());
			answer.put(EzyCommand.PLUGIN_REQUEST_BY_ID, new EzyRequestPluginByIdController());
			answer.put(EzyCommand.PLUGIN_REQUEST_BY_NAME, new EzyRequestPluginByNameController());
			return answer;
		}
		
		@SuppressWarnings("rawtypes")
		protected Map<EzyConstant, EzyInterceptor> newInterceptors() {
			Map<EzyConstant, EzyInterceptor> answer = new ConcurrentHashMap<>();
			answer.put(EzyCommand.PING, EzyInterceptor.ALWAYS_PASS);
			answer.put(EzyCommand.HANDSHAKE, EzyInterceptor.ALWAYS_PASS);
			answer.put(EzyCommand.LOGIN, EzyInterceptor.ALWAYS_PASS);
			answer.put(EzyCommand.APP_ACCESS, new EzyServerUserInterceptor<>());
			answer.put(EzyCommand.APP_REQUEST, new EzyServerUserInterceptor<>());
			answer.put(EzyCommand.PLUGIN_INFO, new EzyServerUserInterceptor<>());
			answer.put(EzyCommand.PLUGIN_REQUEST_BY_ID, new EzyServerUserInterceptor<>());
			answer.put(EzyCommand.PLUGIN_REQUEST_BY_NAME, new EzyServerUserInterceptor<>());
			return answer;
		}
		
		public EzyServerControllers build() {
		    EzyServerControllersImpl answer = new EzyServerControllersImpl();
		    answer.setControllers(newControllers());
		    answer.setInterceptors(newInterceptors());
		    return answer;
		}
	}

}
