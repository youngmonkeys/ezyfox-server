package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyPluginFireEventImpl 
		extends EzyAbstractCommand 
		implements EzyFireEvent {

	private EzyPluginContext context;
	
	public EzyPluginFireEventImpl(EzyPluginContext context) {
		this.context = context;
	}
	
	@SuppressWarnings("rawtypes")
    @Override
	public void fire(EzyConstant type, EzyEvent event) {
	    EzyEventController ctrl = getEventController(type);
	    getLogger().debug("fire event {}, controller = {}", type, ctrl);
	    fire(ctrl, event);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    protected void fire(EzyEventController ctrl, EzyEvent event) {
        if(ctrl != null)
            ctrl.handle(context, event);
    }
	
	@SuppressWarnings("rawtypes")
    protected EzyEventController getEventController(EzyConstant type) {
	    return getEventControllers().getController(type);
	}
	
	protected EzyEventControllers getEventControllers() {
		return getPluginSetting().getEventControllers();
	}
	
	protected EzyPluginSetting getPluginSetting() {
		return context.getPlugin().getSetting();
	}
}
