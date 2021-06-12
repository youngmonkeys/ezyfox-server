package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public class EzyUserRequestAppSingletonController 
		extends EzyUserRequestSingletonController<EzyAppContext, EzyUserRequestAppEvent>
		implements EzyAppRequestController {

	protected EzyUserRequestAppSingletonController(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void responseError(
			EzyAppContext context, 
			EzyUserRequestAppEvent event, EzyData errorData) {
		context.send(errorData, event.getSession(), false);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyUserRequestSingletonController.Builder<Builder> {

		@Override
		public EzyUserRequestAppSingletonController build() {
			return new EzyUserRequestAppSingletonController(this);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		protected EzyUserRequestPrototypeController getPrototypeController() {
			return EzyUserRequestAppPrototypeController.builder()
					.beanContext(beanContext)
					.build();
		}
		
	}
}
