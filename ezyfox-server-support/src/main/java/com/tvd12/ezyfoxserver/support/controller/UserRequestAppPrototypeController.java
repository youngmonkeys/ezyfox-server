package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyAppContextAware;
import com.tvd12.ezyfoxserver.controller.EzyAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class UserRequestAppPrototypeController 
		extends UserRequestPrototypeController<EzyAppContext, EzyUserRequestAppEvent>
		implements EzyAppEventController<EzyUserRequestAppEvent> {

	protected UserRequestAppPrototypeController(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void prehandle(EzyAppContext context, EzyHandler handler) {
		if(handler instanceof EzyAppContextAware)
			((EzyAppContextAware)handler).setAppContext(context);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends UserRequestPrototypeController.Builder<Builder> {
		
		@Override
		public UserRequestAppPrototypeController build() {
			return new UserRequestAppPrototypeController(this);
		}
	}
}
