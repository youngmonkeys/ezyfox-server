package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;

import com.tvd12.ezyfoxserver.appcontroller.EzyUserRequestController;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventAppControllersImpl extends EzyAbstractEventControllers {

	protected EzyEventAppControllersImpl(Builder builder) {
		super(builder);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEventControllers.Builder {
		
		@SuppressWarnings("rawtypes")
		@Override
		protected void addControllers(Map<EzyConstant, EzyEventController> answer) {
			super.addControllers(answer);
			answer.put(EzyEventType.USER_REQUEST, new EzyUserRequestController());
		}
		
		@Override
		public EzyEventControllers build() {
			return new EzyEventAppControllersImpl(this);
		}
	}

}
