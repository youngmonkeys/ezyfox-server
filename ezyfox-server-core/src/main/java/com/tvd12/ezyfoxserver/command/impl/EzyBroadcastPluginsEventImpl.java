package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyBroadcastPluginsEvent;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyBroadcastPluginsEventImpl extends EzyAbstractCommand implements EzyBroadcastPluginsEvent {

	private final EzyZoneContext context;
	private final EzyEventPluginsMapper eventPluginsMapper;
	
	public EzyBroadcastPluginsEventImpl(EzyZoneContext context) {
	    this.context = context;
	    this.eventPluginsMapper = context.getZone().getSetting().getEventPluginsMapper();
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    logger.debug("zone: {} broadcast to plugins event: {}", getZoneName(), type);
		firePluginsEvent(type, event);
	}
	
	protected Set<EzyPluginSetting> getValidPlugins(EzyConstant type) {
	    Set<EzyPluginSetting> valid = eventPluginsMapper.getPlugins(type);
	    return valid;
	}
	
	protected void firePluginsEvent(EzyConstant type, EzyEvent event) {
	    Set<EzyPluginSetting> pluginSettings = getValidPlugins(type);
	    for(EzyPluginSetting pluginSetting : pluginSettings)
	        firePluginEvent(pluginSetting.getId(), type, event);
	}
	
	protected void firePluginEvent(int pluginId, EzyConstant type, EzyEvent event) {
	    EzyPluginContext pluginContext = context.getPluginContext(pluginId);
		firePluginEvent(pluginContext, type, event);
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
}
