package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFireEventImpl extends EzyAbstractCommand implements EzyFireEvent {

	private EzyServerContext context;
	
	@Override
	public void fire(EzyEventType type, EzyEvent event) {
		firePluginsEvent(type, event);
		fireAppsEvent(type, event);
	}
	
	protected void fireAppsEvent(EzyEventType type, EzyEvent event) {
		getAppIds().forEach((appId) -> fireAppEvent(appId, type, event));
	}
	
	protected void fireAppEvent(int appId, EzyEventType type, EzyEvent event) {
		fireAppEvent(context.getAppContext(appId), type, event);
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyEventType type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected void firePluginsEvent(EzyEventType type, EzyEvent event) {
		getPluginIds().forEach((pluginId) -> firePluginEvent(pluginId, type, event));
	}
	
	protected void firePluginEvent(int pluginId, EzyEventType type, EzyEvent event) {
		firePluginEvent(context.getPluginContext(pluginId), type, event);
	}
	
	protected void firePluginEvent(EzyPluginContext ctx, EzyEventType type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected Set<Integer> getAppIds() {
		return getServer().getAppIds();
	}
	
	protected Set<Integer> getPluginIds() {
		return getServer().getPluginIds();
	}
	
	protected EzyServer getServer() {
		return context.getBoss();
	}
}
