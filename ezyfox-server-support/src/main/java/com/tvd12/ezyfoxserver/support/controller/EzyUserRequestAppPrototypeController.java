package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyAppContextAware;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class EzyUserRequestAppPrototypeController 
		extends EzyUserRequestPrototypeController<EzyAppContext, EzyUserRequestAppEvent>
		implements EzyAppRequestController {

	protected EzyUserRequestAppPrototypeController(Builder builder) {
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
	
	public static class Builder extends EzyUserRequestPrototypeController.Builder<Builder> {
		
		@Override
		public EzyUserRequestAppPrototypeController build() {
			return new EzyUserRequestAppPrototypeController(this);
		}
	}
}
