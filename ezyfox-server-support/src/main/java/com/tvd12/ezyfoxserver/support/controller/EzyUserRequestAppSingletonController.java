package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class EzyUserRequestAppSingletonController 
		extends EzyUserRequestSingletonController<EzyAppContext, EzyUserRequestAppEvent>
		implements EzyAppEventController<EzyUserRequestAppEvent> {

	protected EzyUserRequestAppSingletonController(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyUserRequestSingletonController.Builder<Builder> {

		@Override
		public EzyUserRequestAppSingletonController build() {
			return new EzyUserRequestAppSingletonController(this);
		}
		
	}
}
