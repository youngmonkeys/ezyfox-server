package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.handleException;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFireEventImpl extends EzyAbstractCommand implements EzyFireEvent {

    private final EzyServerContext context;

    @Override
    public void fire(EzyConstant type, EzyEvent event) {
        getLogger().debug("fire server event: {}", type);
        fireZonesEvent(type, event);
    }

    protected void fireZonesEvent(EzyConstant type, EzyEvent event) {
        for(EzyZoneContext zoneContext : context.getZoneContexts())
            fireZoneEvent(zoneContext, type, event);
    }

    protected void fireZoneEvent(EzyZoneContext ctx, EzyConstant type, EzyEvent event) {
        try {
            ctx.fireEvent(type, event);
        } catch (Exception e) {
            handleException(ctx, Thread.currentThread(), e);
        }
    }

}