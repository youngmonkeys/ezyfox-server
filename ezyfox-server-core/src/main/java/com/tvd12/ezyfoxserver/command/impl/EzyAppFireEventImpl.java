package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyAppContexts.handleException;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EzyAppFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyAppContext context;
	
	public EzyAppFireEventImpl(EzyAppContext context) {
		this.context = context;
	}
	
    @Override
	public void fire(EzyConstant type, EzyEvent event) {
	    EzyEventController ctrl = getController(type);
	    getLogger().debug("fire event {}, controller = {}", type, ctrl);
		fire(ctrl, event);
	}
	
	protected void fire(EzyEventController ctrl, EzyEvent event) {
		if(ctrl != null)
			handle(ctrl, event);
	}
	
	protected void handle(EzyEventController ctrl, EzyEvent event) {
	    try {
	        ctrl.handle(context, event);
	    }
	    catch(Exception e) {
	        handleException(context, Thread.currentThread(), e);
	    }
	}
	
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
