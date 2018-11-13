package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyHashMapSet;
import com.tvd12.ezyfox.util.EzyMapSet;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastPluginsEvent;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyBroadcastPluginsEventImpl extends EzyAbstractCommand implements EzyBroadcastPluginsEvent {

	private final EzyZoneContext context;
	private final EzyMapSet<EzyConstant, EzyPluginContext> pluginContextss;
	
	public EzyBroadcastPluginsEventImpl(EzyZoneContext context) {
	    this.context = context;
	    this.pluginContextss = getPluginContextss();
	    
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    logger.debug("zone: {} broadcast to plugins event: {}", getZoneName(), type);
		Set<EzyPluginContext> pluginContexts = pluginContextss.get(type);
		if(pluginContexts != null) {
		    for(EzyPluginContext pluginContext : pluginContexts)
		        firePluginEvent(pluginContext, type, event);
		}
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event) {
	    try {
	        ctx.handleEvent(type, event);
	    }
	    catch(Exception e) {
	        ctx.handleException(Thread.currentThread(), e);
	    }
	}
	
	protected String getZoneName() {
	    return context.getZone().getSetting().getName();
	}
	
	private EzyMapSet<EzyConstant, EzyPluginContext> getPluginContextss() {
        EzyZoneSetting zoneSetting = context.getZone().getSetting();
        EzyEventPluginsMapper eventPluginsMapper = zoneSetting.getEventPluginsMapper();
        EzyMapSet<EzyConstant, EzyPluginContext> pluginContextss = new EzyHashMapSet<>();
        EzyMapSet<EzyConstant, EzyPluginSetting> eventsPluginss = eventPluginsMapper.getEventsPluginss();
        for(EzyConstant type : eventsPluginss.keySet()) {
            Set<EzyPluginSetting> pluginSettings = eventsPluginss.get(type);
            for(EzyPluginSetting pluginSetting : pluginSettings) {
                int pluginId = pluginSetting.getId();
                EzyPluginContext pluginContext = context.getPluginContext(pluginId);
                pluginContextss.addItem(type, pluginContext);
            }
        }
        return pluginContextss;
    }
}
