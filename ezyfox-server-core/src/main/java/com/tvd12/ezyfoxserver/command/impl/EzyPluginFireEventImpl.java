package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.config.EzyPlugin;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyPluginFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyPluginContext context;
	
	public EzyPluginFireEventImpl(EzyPluginContext context) {
		this.context = context;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
		getEventControllers().getController(type).handle(context, event);
	}
	
	protected EzyEventControllers getEventControllers() {
		return getPlugin().getEventControllers();
	}
	
	protected EzyPlugin getPlugin() {
		return context.getPlugin();
	}
}