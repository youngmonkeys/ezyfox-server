package com.tvd12.ezyfoxserver.delegate;

import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.forEachAppContexts;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
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
        EzyUserEvent event = newUserRemovedEvent(user, reason);
        notifyToPlugins(zoneContext, event);
        removeUserFromApps(zoneContext, user);
    }
    
    protected void notifyToPlugins(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.get(EzyFirePluginEvent.class)
                   .fire(EzyEventType.USER_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify user: " + event.getUser() + " removed error", e);
        }
    }
    
    protected void notifyToApps(EzyZoneContext context, EzyUserEvent event) {
        try {
            context.get(EzyFireAppEvent.class)
                .filter(appCtxt -> containsUser(appCtxt, event.getUser()))
                .fire(EzyEventType.USER_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify user: " + event.getUser() + " disconnect to server error", e);
        }
    }
    
    protected void removeUserFromApps(EzyZoneContext context, EzyUser user) {
        forEachAppContexts(context, ctx -> removeUserFromApp(ctx, user));
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
