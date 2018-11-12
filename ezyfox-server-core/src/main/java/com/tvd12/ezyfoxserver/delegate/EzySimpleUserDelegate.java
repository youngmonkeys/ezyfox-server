package com.tvd12.ezyfoxserver.delegate;

import java.util.Collection;

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
        removeUserFromApps(zoneContext, user); 
        EzyUserEvent event = newUserRemovedEvent(user, reason);
        notifyToApps(zoneContext, event);
        notifyToPlugins(zoneContext, event);
    }
    
    protected void notifyToPlugins(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.broadcastPlugins(EzyEventType.USER_REMOVED, event);
        }
        catch(Exception e) {
            String zoneName = context.getZone().getSetting().getName();
            logger.error("zone: " + zoneName + ", notify to plugins user: " + event.getUser() + " removed error", e);
        }
    }
    
    protected void notifyToApps(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.broadcastApps(EzyEventType.USER_REMOVED, event, event.getUser());
        }
        catch(Exception e) {
            String zoneName = context.getZone().getSetting().getName();
            logger.error("zone: " + zoneName + ", notify to apps user: " + event.getUser() + " removed error", e);
        }
    }
    
    protected void removeUserFromApps(EzyZoneContext context, EzyUser user) {
        Collection<EzyAppContext> appContexts = context.getAppContexts();
        for(EzyAppContext appCtx : appContexts)
            removeUserFromApp(appCtx, user);
    }
    
    protected void removeUserFromApp(EzyAppContext ctx, EzyUser user) {
        EzyUserManager userManager = ctx.getApp().getUserManager();
        if(userManager.containsUser(user))
            userManager.removeUser(user);
    }
    
    protected EzyUserEvent newUserRemovedEvent(EzyUser user, EzyConstant reason) {
        return new EzySimpleUserRemovedEvent(user, reason);
    }
    
}
