package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContextAware;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;

public class EzyUserRequestPluginPrototypeController 
		extends EzyUserRequestPrototypeController<EzyPluginContext, EzyUserRequestPluginEvent>
		implements EzyPluginRequestController {

	protected EzyUserRequestPluginPrototypeController(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void preHandle(
			EzyPluginContext context,
			EzyUserRequestPluginEvent event, EzyHandler handler) {
		if(handler instanceof EzyPluginContextAware)
			((EzyPluginContextAware)handler).setPluginContext(context);
	}
	
	@Override
	protected void responseError(
			EzyPluginContext context, 
			EzyUserRequestPluginEvent event, EzyData errorData) {
		context.send(errorData, event.getSession());
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyUserRequestPrototypeController.Builder<Builder> {
		@Override
		public EzyUserRequestPluginPrototypeController build() {
			return new EzyUserRequestPluginPrototypeController(this);
		}
	}
}
