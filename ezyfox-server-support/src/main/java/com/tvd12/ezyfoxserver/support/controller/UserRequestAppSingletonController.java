package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class UserRequestAppSingletonController 
		extends UserRequestSingletonController<EzyAppContext, EzyUserRequestAppEvent>
		implements EzyAppEventController<EzyUserRequestAppEvent> {

	protected UserRequestAppSingletonController(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends UserRequestSingletonController.Builder<Builder> {

		@Override
		public UserRequestAppSingletonController build() {
			return new UserRequestAppSingletonController(this);
		}
		
	}
}
