package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyAppFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyAppContext context;
	
	public EzyAppFireEventImpl(EzyAppContext context) {
		this.context = context;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fire(EzyEventType type, EzyEvent event) {
		getEventControllers().getController(type).handle(context, event);
	}
	
	protected EzyEventControllers getEventControllers() {
		return getApp().getEventControllers();
	}
	
	protected EzyApp getApp() {
		return context.getApp();
	}
}
