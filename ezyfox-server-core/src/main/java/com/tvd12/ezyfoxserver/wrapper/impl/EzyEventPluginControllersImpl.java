package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.Map;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.plugincontroller.EzyServerReadyController;
import com.tvd12.ezyfoxserver.plugincontroller.EzyUserRequestPluginController;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventPluginControllersImpl extends EzyAbstractEventControllers {

	protected EzyEventPluginControllersImpl(Builder builder) {
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
	        answer.put(EzyEventType.SERVER_READY, new EzyServerReadyController());
	        answer.put(EzyEventType.USER_REQUEST, new EzyUserRequestPluginController());
	    }
	    
		@Override
		public EzyEventControllers build() {
			return new EzyEventPluginControllersImpl(this);
		}
	}

}
