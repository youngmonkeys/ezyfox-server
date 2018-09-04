package com.tvd12.ezyfoxserver.command.impl;

import static com.tvd12.ezyfoxserver.context.EzyPluginContexts.handleException;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
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
	    getLogger().debug("fire event: {}", type);
		firePluginsEvent(type, event);
	}
	
	protected Set<EzyPluginSetting> getValidPlugins(EzyConstant type) {
	    return eventPluginsMapper.getPlugins(type);
	}
	
	protected void firePluginsEvent(EzyConstant type, EzyEvent event) {
	    getValidPlugins(type).forEach(plugin -> 
	        firePluginEvent(plugin.getId(), type, event)
	    );
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
	        handleException(ctx, Thread.currentThread(), e);
	    }
	}
}
