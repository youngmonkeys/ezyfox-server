package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyAppContexts.handleException;
import static com.tvd12.ezyfoxserver.context.EzyPluginContexts.handleException;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyZoneFireEventImpl extends EzyAbstractCommand implements EzyFireEvent {

	private final EzyZoneContext context;
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    getLogger().debug("zone: {} fire event: {}", getZoneName(), type);
		firePluginsEvent(type, event);
		fireAppsEvent(type, event);
	}
	
	protected void fireAppsEvent(EzyConstant type, EzyEvent event) {
	    for(EzyAppContext appContext : context.getAppContexts())
	        fireAppEvent(appContext, type, event);
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event) {
	    try {
	        ctx.fireEvent(type, event);
	    }
	    catch(Exception e) {
	        handleException(ctx, Thread.currentThread(), e);
	    }
	}
	
	protected void firePluginsEvent(EzyConstant type, EzyEvent event) {
	    for(EzyPluginContext pluginContext : context.getPluginContexts())
	        firePluginEvent(pluginContext, type, event);
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event) {
	    try {
	        ctx.fireEvent(type, event);
	    }
	    catch(Exception e) {
	        handleException(ctx, Thread.currentThread(), e);
	    }
	}
	
	protected String getZoneName() {
	    return context.getZone().getSetting().getName();
	}
	
}
