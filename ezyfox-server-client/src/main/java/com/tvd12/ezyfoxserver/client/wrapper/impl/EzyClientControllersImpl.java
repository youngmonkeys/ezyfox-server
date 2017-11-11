package com.tvd12.ezyfoxserver.client.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.client.controller.EzyConnectFailureController;
import com.tvd12.ezyfoxserver.client.controller.EzyConnectSuccessController;
import com.tvd12.ezyfoxserver.client.controller.EzyDisconnectController;
import com.tvd12.ezyfoxserver.client.controller.EzyErrorController;
import com.tvd12.ezyfoxserver.client.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.client.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.client.controller.EzyLoginErrorController;
import com.tvd12.ezyfoxserver.client.controller.EzyPongController;
import com.tvd12.ezyfoxserver.client.controller.EzyRequestAppController;
import com.tvd12.ezyfoxserver.client.wrapper.EzyClientControllers;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Setter;

@Setter
public class EzyClientControllersImpl implements EzyClientControllers {
	
	protected Map<EzyConstant, Object> controllers;
	
	protected EzyClientControllersImpl(Builder builder) {
		this.controllers = builder.newControllers();
	}
	
	@Override 
	public Object getController(EzyConstant cmd) {
		return controllers.get(cmd);
	} 
	
	@Override
	public void addController(EzyConstant cmd, Object ctrl) {
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

	public static class Builder implements EzyBuilder<EzyClientControllers> {
		
		protected Map<EzyConstant, Object> newControllers() {
			Map<EzyConstant, Object> answer = new ConcurrentHashMap<>();
			answer.put(EzyClientCommand.PONG, new EzyPongController());
			answer.put(EzyClientCommand.ERROR, new EzyErrorController());
			answer.put(EzyClientCommand.CONNECT_SUCCESS, new EzyConnectSuccessController());
			answer.put(EzyClientCommand.CONNECT_FAILURE, EzyConnectFailureController.EMPTY);
			answer.put(EzyClientCommand.HANDSHAKE, new EzyHandShakeController());
			answer.put(EzyClientCommand.LOGIN, new EzyLoginController());
			answer.put(EzyClientCommand.LOGIN_ERROR, new EzyLoginErrorController());
			answer.put(EzyClientCommand.APP_ACCESS, new EzyAccessAppController());
			answer.put(EzyClientCommand.DISCONNECT, new EzyDisconnectController());
			answer.put(EzyClientCommand.APP_REQUEST, new EzyRequestAppController());
			return answer;
		}
		
		@Override
		public EzyClientControllers build() {
			return new EzyClientControllersImpl(this);
		}
	}
	
}
