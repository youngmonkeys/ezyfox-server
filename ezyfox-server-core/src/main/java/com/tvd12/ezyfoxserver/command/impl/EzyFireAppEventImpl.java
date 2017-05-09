package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;
import java.util.function.Predicate;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

public class EzyFireAppEventImpl extends EzyAbstractCommand implements EzyFireAppEvent {

	protected EzyServerContext context;
	protected Predicate<EzyAppContext> filter;
	
	public EzyFireAppEventImpl(EzyServerContext context) {
	    this.context = context;
	}
	
	@Override
	public EzyFireAppEvent filter(Predicate<EzyAppContext> filter) {
	    this.filter = filter;
	    return this;
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    getLogger().debug("fire event {}", type);
		fireAppsEvent(type, event);
	}
	
	protected void fireAppsEvent(EzyConstant type, EzyEvent event) {
		getAppIds().forEach((appId) -> fireAppEvent(appId, type, event));
	}
	
	protected void fireAppEvent(int appId, EzyConstant type, EzyEvent event) {
	    EzyAppContext appCtxt = context.getAppContext(appId);
	    if(shouldFireAppEvent(appCtxt)) 
	        fireAppEvent(appCtxt, type, event);
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected boolean shouldFireAppEvent(EzyAppContext appContext) {
	    return filter != null && filter.test(appContext);
	}
	
	protected Set<Integer> getAppIds() {
		return getServer().getAppIds();
	}
	
	protected EzyServer getServer() {
		return context.getServer();
	}
}
