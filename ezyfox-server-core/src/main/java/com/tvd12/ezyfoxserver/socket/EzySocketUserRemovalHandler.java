package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class EzySocketUserRemovalHandler extends EzySocketAbstractEventHandler {

	protected final EzySocketUserRemovalQueue userRemovalQueue;
	
	public EzySocketUserRemovalHandler() {
	    this.userRemovalQueue = EzyBlockingSocketUserRemovalQueue.getInstance();
	}
	
	@Override
    public void handleEvent() {
	    processUserRemovalQueue();
	}
	
	@Override
	public void destroy() {
	    processWithLogException(userRemovalQueue::clear);
	}
	
	private void processUserRemovalQueue() {
		try {
			EzySocketUserRemoval removal = userRemovalQueue.take();
			processUserRemoval(removal);
		} 
		catch (InterruptedException e) {
			logger.warn("user-removal-handler thread interrupted: {}", Thread.currentThread());
		}
		catch(Throwable throwable) {
			logger.warn("problems in user-removal-handler, thread: {}", Thread.currentThread(), throwable);
		}
	}
	
	private void processUserRemoval(EzySocketUserRemoval removal) {
	    try {
	        processUserRemoval0(removal);
	    }
	    finally {
	        removal.release();
        }
	}
	
	private void processUserRemoval0(EzySocketUserRemoval removal) {
	    EzyUser user = removal.getUser();
	    try {
    	        EzyConstant reason = removal.getReason();
    	        EzyZoneContext zoneContext = removal.getZoneContext();
            Set<EzyAppContext> appContexts = removeUserFromApps(zoneContext, user); 
            EzyUserEvent event = newUserRemovedEvent(user, reason);
            notifyUserRemovedToApps(zoneContext, appContexts, event);
            notifyUserRemovedToPlugins(zoneContext, event);
	    }
	    finally {
            user.destroy();
        }
	    logger.debug("user {} has destroyed", user);
	}
	
	protected void notifyUserRemovedToPlugins(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.broadcastPlugins(EzyEventType.USER_REMOVED, event, true);
        }
        catch(Exception e) {
            String zoneName = context.getZone().getSetting().getName();
            logger.error("zone: {}, notify to plugins user: {} removed error", zoneName, event.getUser(), e);
        }
    }
    
    protected void notifyUserRemovedToApps(EzyZoneContext zoneContext, Set<EzyAppContext> appContexts, EzyUserEvent event) {
        for(EzyAppContext appContext : appContexts)
            notifyUserRemovedToApp(zoneContext, appContext, event);
    }
    
    protected void notifyUserRemovedToApp(EzyZoneContext zoneContext, EzyAppContext appContext, EzyUserEvent event) {
        try {
            appContext.handleEvent(EzyEventType.USER_REMOVED, event);
        }
        catch(Exception e) {
            String zoneName = zoneContext.getZone().getSetting().getName();
            logger.error("zone: {}, notify to apps user: {} removed error", zoneName, event.getUser(), e);
        }
    }
    
    protected Set<EzyAppContext> removeUserFromApps(EzyZoneContext context, EzyUser user) {
        Set<EzyAppContext> containAppContexts = new HashSet<>();
        Collection<EzyAppContext> appContexts = context.getAppContexts();
        for(EzyAppContext appCtx : appContexts) {
            boolean contains = removeUserFromApp(appCtx, user);
            if(contains)
                containAppContexts.add(appCtx);
        }
        return containAppContexts;
    }
    
    protected boolean removeUserFromApp(EzyAppContext ctx, EzyUser user) {
        EzyUserManager userManager = ctx.getApp().getUserManager();
        boolean contains = userManager.containsUser(user);
        if(contains)
            userManager.removeUser(user);
        return contains;
    }
    
    protected EzyUserEvent newUserRemovedEvent(EzyUser user, EzyConstant reason) {
        return new EzySimpleUserRemovedEvent(user, reason);
    }
}
