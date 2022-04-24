package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyBroadcastEventImpl extends EzyAbstractCommand implements EzyBroadcastEvent {

    private final EzyServerContext context;

    @Override
    public void fire(EzyConstant type, EzyEvent event, boolean catchException) {
        logger.debug("broadcast server event: {}", type);
        fireZonesEvent(type, event, catchException);
    }

    protected void fireZonesEvent(EzyConstant type, EzyEvent event, boolean catchException) {
        for (EzyZoneContext zoneContext : context.getZoneContexts()) {
            fireZoneEvent(zoneContext, type, event, catchException);
        }
    }

    protected void fireZoneEvent(EzyZoneContext ctx, EzyConstant type, EzyEvent event, boolean catchException) {
        if (catchException) {
            try {
                ctx.handleEvent(type, event);
                ctx.broadcast(type, event, catchException);
            } catch (Exception e) {
                ctx.handleException(Thread.currentThread(), e);
            }
        } else {
            ctx.handleEvent(type, event);
            ctx.broadcast(type, event, catchException);
        }
    }

}
