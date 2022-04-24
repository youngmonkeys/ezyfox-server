package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastAppsEvent;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import java.util.function.Predicate;

import static com.tvd12.ezyfoxserver.context.EzyAppContexts.containsUser;

public class EzyBroadcastAppsEventImpl extends EzyAbstractCommand implements EzyBroadcastAppsEvent {

    private final EzyZoneContext context;

    public EzyBroadcastAppsEventImpl(EzyZoneContext context) {
        this.context = context;
    }

    @Override
    public void fire(EzyConstant type, EzyEvent event, boolean catchException) {
        logger.debug("zone {} broadcast to apps event: {}", getZoneName(), type);
        for (EzyAppContext appContext : context.getAppContexts()) {
            fireAppEvent(appContext, type, event, catchException);
        }
    }

    @Override
    public void fire(EzyConstant type, EzyEvent event, String username, boolean catchException) {
        fire(type, event, appCtx -> containsUser(appCtx, username), catchException);
    }

    @Override
    public void fire(EzyConstant type, EzyEvent event, EzyUser user, boolean catchException) {
        fire(type, event, appCtx -> containsUser(appCtx, user), catchException);
    }

    @Override
    public void fire(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter, boolean catchException) {
        logger.debug("zone {} broadcast to apps event: {}", getZoneName(), type);
        for (EzyAppContext appContext : context.getAppContexts()) {
            if (filter.test(appContext)) {
                fireAppEvent(appContext, type, event, catchException);
            }
        }
    }

    protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event, boolean catchException) {
        if (catchException) {
            try {
                ctx.handleEvent(type, event);
            } catch (Exception e) {
                ctx.handleException(Thread.currentThread(), e);
            }
        } else {
            ctx.handleEvent(type, event);
        }
    }

    protected String getZoneName() {
        return context.getZone().getSetting().getName();
    }
}
