package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.handleException;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFireEventImpl extends EzyAbstractCommand implements EzyFireEvent {

    private EzyServerContext context;

    @Override
    public void fire(EzyConstant type, EzyEvent event) {
        getLogger().debug("fire event: {}", type);
        fireZonesEvent(type, event);
    }

    protected void fireZonesEvent(EzyConstant type, EzyEvent event) {
        getZoneIds().forEach(zoneId -> fireZoneEvent(zoneId, type, event));
    }

    protected void fireZoneEvent(int zoneId, EzyConstant type, EzyEvent event) {
        fireZoneEvent(context.getZoneContext(zoneId), type, event);
    }

    protected void fireZoneEvent(EzyZoneContext ctx, EzyConstant type, EzyEvent event) {
        try {
            ctx.get(EzyFireEvent.class).fire(type, event);
        } catch (Exception e) {
            handleException(ctx, Thread.currentThread(), e);
        }
    }

    protected Set<Integer> getZoneIds() {
        return context.getServer().getSettings().getZoneIds();
    }

}