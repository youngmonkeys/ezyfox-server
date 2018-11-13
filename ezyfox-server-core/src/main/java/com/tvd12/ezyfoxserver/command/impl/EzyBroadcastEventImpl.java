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
    public void fire(EzyConstant type, EzyEvent event) {
        logger.debug("broadcast server event: {}", type);
        fireZonesEvent(type, event);
    }

    protected void fireZonesEvent(EzyConstant type, EzyEvent event) {
        for(EzyZoneContext zoneContext : context.getZoneContexts())
            fireZoneEvent(zoneContext, type, event);
    }

    protected void fireZoneEvent(EzyZoneContext ctx, EzyConstant type, EzyEvent event) {
        try {
            ctx.handleEvent(type, event);
            ctx.broadcast(type, event);
        } catch (Exception e) {
            ctx.handleException(Thread.currentThread(), e);
        }
    }

}