package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyHashMapSet;
import com.tvd12.ezyfox.util.EzyMapSet;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastPluginsEvent;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

import java.util.Collection;
import java.util.Set;

public class EzyBroadcastPluginsEventImpl extends EzyAbstractCommand implements EzyBroadcastPluginsEvent {

    private final EzyZoneContext context;
    private final EzyMapSet<EzyConstant, EzyPluginContext> pluginContextss;

    public EzyBroadcastPluginsEventImpl(EzyZoneContext context) {
        this.context = context;
        this.pluginContextss = getPluginContextss();

    }

    @Override
    public void fire(EzyConstant type, EzyEvent event, boolean catchException) {
        logger.debug("zone: {} broadcast to plugins event: {}", getZoneName(), type);
        Set<EzyPluginContext> pluginContexts = pluginContextss.get(type);
        if (pluginContexts != null) {
            for (EzyPluginContext pluginContext : pluginContexts) {
                firePluginEvent(pluginContext, type, event, catchException);
            }
        }
    }

    protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event, boolean catchException) {
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

    private EzyMapSet<EzyConstant, EzyPluginContext> getPluginContextss() {
        Collection<EzyPluginContext> pluginContexts = context.getPluginContexts();
        EzyMapSet<EzyConstant, EzyPluginContext> pluginContextss = new EzyHashMapSet<>();
        for (EzyPluginContext pluginContext : pluginContexts) {
            EzyPluginSetting pluginSetting = pluginContext.getPlugin().getSetting();
            Set<EzyConstant> listenEvents = pluginSetting.getListenEvents().getEvents();
            for (EzyConstant listenEvent : listenEvents) {
                pluginContextss.addItem(listenEvent, pluginContext);
            }
        }
        return pluginContextss;
    }
}
