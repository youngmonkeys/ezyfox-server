package com.tvd12.ezyfoxserver.delegate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

import lombok.Setter;

@Setter
public class EzySimpleUserDelegate
        extends EzyLoggable
        implements EzyUserDelegate {

    protected EzyServerContext serverContext;
    
    public EzySimpleUserDelegate(EzyServerContext serverContext) {
        this.serverContext = serverContext;
    }
    
    @Override
    public void onUserRemoved(EzyUser user, EzyConstant reason) {
        EzyZoneContext zoneContext = serverContext.getZoneContext(user.getZoneId());
        Set<EzyAppContext> appContexts = removeUserFromApps(zoneContext, user); 
        EzyUserEvent event = newUserRemovedEvent(user, reason);
        notifyUserRemovedToApps(zoneContext, appContexts, event);
        notifyUserRemovedToPlugins(zoneContext, event);
    }
    
    protected void notifyUserRemovedToPlugins(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.broadcastPlugins(EzyEventType.USER_REMOVED, event);
        }
        catch(Exception e) {
            String zoneName = context.getZone().getSetting().getName();
            logger.error("zone: " + zoneName + ", notify to plugins user: " + event.getUser() + " removed error", e);
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
            logger.error("zone: " + zoneName + ", notify to apps user: " + event.getUser() + " removed error", e);
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
