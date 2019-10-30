package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyZoneBroadcastEventImpl extends EzyAbstractCommand implements EzyBroadcastEvent {

	private final EzyZoneContext context;
	
	@Override
	public void fire(EzyConstant type, EzyEvent event, boolean catchException) {
	    logger.debug("zone: {} fire event: {}", getZoneName(), type);
		firePluginsEvent(type, event, catchException);
		fireAppsEvent(type, event, catchException);
	}
	
	protected void fireAppsEvent(EzyConstant type, EzyEvent event, boolean catchException) {
	    for(EzyAppContext appContext : context.getAppContexts())
	        fireAppEvent(appContext, type, event, catchException);
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event, boolean catchException) {
	    if(catchException) {
    	    try {
    	        ctx.handleEvent(type, event);
    	    }
    	    catch(Exception e) {
    	        ctx.handleException(Thread.currentThread(), e);
    	    }
	    }
	    else {
	        ctx.handleEvent(type, event);
	    }
	}
	
	protected void firePluginsEvent(EzyConstant type, EzyEvent event, boolean catchException) {
	    for(EzyPluginContext pluginContext : context.getPluginContexts())
	        firePluginEvent(pluginContext, type, event, catchException);
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event, boolean catchException) {
	    if(catchException) {
    	    try {
    	        ctx.handleEvent(type, event);
    	    }
    	    catch(Exception e) {
    	        ctx.handleException(Thread.currentThread(), e);
    	    }
	    }
	    else {
	        ctx.handleEvent(type, event);
	    }
	}
	
	protected String getZoneName() {
	    return context.getZone().getSetting().getName();
	}
	
}
