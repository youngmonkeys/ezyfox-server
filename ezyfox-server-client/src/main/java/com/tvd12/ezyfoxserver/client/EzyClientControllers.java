package com.tvd12.ezyfoxserver.client;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.client.controller.EzyAccessAppController;
import com.tvd12.ezyfoxserver.client.controller.EzyDisconnectController;
import com.tvd12.ezyfoxserver.client.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.client.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyControllersImpl;

public class EzyClientControllers extends EzyControllersImpl {
	
	protected EzyClientControllers(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyControllersImpl.Builder {
		
		@SuppressWarnings("rawtypes")
		@Override
		protected Map<EzyConstant, EzyController> newControllers() {
			Map<EzyConstant, EzyController> answer = new HashMap<>();
			answer.put(EzyCommand.HAND_SHAKE, new EzyHandShakeController());
			answer.put(EzyCommand.LOGIN, new EzyLoginController());
			answer.put(EzyCommand.ACCESS_APP, new EzyAccessAppController());
			answer.put(EzyCommand.DISCONNECT, new EzyDisconnectController());
			return answer;
		}
		
		@Override
		public EzyControllers build() {
			return new EzyClientControllers(this);
		}
	}

}
