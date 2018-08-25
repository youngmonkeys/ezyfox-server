package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;

public class UserRequestPluginSingletonController 
		extends UserRequestSingletonController<EzyPluginContext, EzyUserRequestPluginEvent>
		implements EzyPluginEventController<EzyUserRequestPluginEvent> {

	protected UserRequestPluginSingletonController(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends UserRequestSingletonController.Builder<Builder> {

		@Override
		public UserRequestPluginSingletonController build() {
			return new UserRequestPluginSingletonController(this);
		}
		
	}
}
