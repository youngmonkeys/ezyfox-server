package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyAppFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyAppContext context;
	
	public EzyAppFireEventImpl(EzyAppContext context) {
		this.context = context;
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
		fire(getController(type), event);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void fire(EzyEventController ctrl, EzyEvent event) {
		if(ctrl != null)
			ctrl.handle(context, event);
	}
	
	@SuppressWarnings("rawtypes")
	protected EzyEventController getController(EzyConstant type) {
		return getEventControllers().getController(type);
	}
	
	protected EzyEventControllers getEventControllers() {
		return getApp().getEventControllers();
	}
	
	protected EzyApp getApp() {
		return context.getApp();
	}
}
