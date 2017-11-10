package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyAppFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyAppContext context;
	
	public EzyAppFireEventImpl(EzyAppContext context) {
		this.context = context;
	}
	
	@SuppressWarnings("rawtypes")
    @Override
	public void fire(EzyConstant type, EzyEvent event) {
	    EzyEventController ctrl = getController(type);
	    getLogger().debug("fire event {}, controller = {}", type, ctrl);
		fire(ctrl, event);
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
		return getAppSetting().getEventControllers();
	}
	
	protected EzyAppSetting getAppSetting() {
		return context.getApp().getSetting();
	}
}
