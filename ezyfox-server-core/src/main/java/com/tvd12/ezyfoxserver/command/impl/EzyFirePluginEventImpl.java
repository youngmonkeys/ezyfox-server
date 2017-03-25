package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFirePluginEventImpl 
		extends EzyAbstractCommand 
		implements EzyFirePluginEvent {

	private EzyServerContext context;
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
		firePluginsEvent(type, event);
	}
	
	protected void firePluginsEvent(EzyConstant type, EzyEvent event) {
		getPluginIds().forEach((pluginId) -> firePluginEvent(pluginId, type, event));
	}
	
	protected void firePluginEvent(int pluginId, EzyConstant type, EzyEvent event) {
		firePluginEvent(context.getPluginContext(pluginId), type, event);
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyConstant type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected Set<Integer> getPluginIds() {
		return getServer().getPluginIds();
	}
	
	protected EzyServer getServer() {
		return context.getBoss();
	}
}
