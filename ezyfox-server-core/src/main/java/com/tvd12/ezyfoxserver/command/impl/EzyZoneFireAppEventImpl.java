package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyAppContexts.handleException;

import java.util.function.Predicate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import static com.tvd12.ezyfoxserver.context.EzyAppContexts.*;

public class EzyZoneFireAppEventImpl extends EzyAbstractCommand implements EzyFireAppEvent {

    private final EzyZoneContext context;
	
	public EzyZoneFireAppEventImpl(EzyZoneContext context) {
	    this.context = context;
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    getLogger().debug("zone {} fire event: {}", getZoneName(), type);
	    for(EzyAppContext appContext : context.getAppContexts())
            fireAppEvent(appContext, type, event);
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event, String username) {
	    fire(type, event, appCtx -> containsUser(appCtx, username));
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event, EzyUser user) {
	    fire(type, event, appCtx -> containsUser(appCtx, user));
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter) {
	    getLogger().debug("zone {} fire event: {}", getZoneName(), type);
	    for(EzyAppContext appContext : context.getAppContexts()) {
            if(filter.test(appContext)) 
                fireAppEvent(appContext, type, event);
        }
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event) {
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
