package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyZoneFirePluginEventImpl 
		extends EzyAbstractCommand 
		implements EzyFirePluginEvent {

	private final EzyZoneContext context;
	private final EzyEventPluginsMapper eventPluginsMapper;
	
	public EzyZoneFirePluginEventImpl(EzyZoneContext context) {
	    this.context = context;
	    this.eventPluginsMapper = context.getZone().getSetting().getEventPluginsMapper();
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    getLogger().debug("zone: {} fire plugin event: {}", getZoneName(), type);
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
	        ctx.fireEvent(type, event);
	    }
	    catch(Exception e) {
	        ctx.handleException(Thread.currentThread(), e);
	    }
	}
	
	protected String getZoneName() {
	    return context.getZone().getSetting().getName();
	}
}
