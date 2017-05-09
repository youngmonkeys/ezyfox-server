package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;

public class EzyFirePluginEventImpl 
		extends EzyAbstractCommand 
		implements EzyFirePluginEvent {

	private final EzyServerContext context;
	private final EzyEventPluginsMapper eventPluginsMapper;
	
	public EzyFirePluginEventImpl(EzyServerContext context) {
	    this.context = context;
	    this.eventPluginsMapper = context.getServer().getEventPluginsMapper();
	}
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
	    getLogger().debug("fire event {}", type);
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
		firePluginEvent(context.getPluginContext(pluginId), type, event);
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
}
