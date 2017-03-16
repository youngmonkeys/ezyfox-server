package com.tvd12.ezyfoxserver.context;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.impl.EzyFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendMessageImpl;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.config.EzyPlugin;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleContext extends EzyAbstractContext implements EzyServerContext {

	@Setter
	@Getter
	protected EzyServer boss;
	
	protected Map<Integer, EzyAppContext> appContextsById;
	protected Map<String, EzyAppContext> appContextsByName;
	protected Map<Integer, EzyPluginContext> pluginContextsById;
	protected Map<String, EzyPluginContext> pluginContextsByName;
	
	{
		appContextsById = new ConcurrentHashMap<>();
		appContextsByName = new ConcurrentHashMap<>();
		pluginContextsById = new ConcurrentHashMap<>();
		pluginContextsByName = new ConcurrentHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	public void addAppContext(EzyApp app, EzyAppContext appContext) {
		appContextsById.putIfAbsent(app.getId(), appContext);
		appContextsByName.putIfAbsent(app.getName(), appContext);
	}
	
	public void addAppContexts(Collection<EzyAppContext> appContexts) {
		for(EzyAppContext ctx : appContexts)
			addAppContext(ctx.getApp(), ctx);
	}
	
	public void addPluginContext(EzyPlugin plugin, EzyPluginContext pluginContext) {
		pluginContextsById.putIfAbsent(plugin.getId(), pluginContext);
		pluginContextsByName.putIfAbsent(plugin.getName(), pluginContext);
	}
	
	public void addPluginContexts(Collection<EzyPluginContext> pluginContexts) {
		for(EzyPluginContext ctx : pluginContexts)
			addPluginContext(ctx.getPlugin(), ctx);
	}
	
	@Override
	public EzyAppContext getAppContext(int appId) {
		if(appContextsById.containsKey(appId))
			return appContextsById.get(appId);
		throw new IllegalArgumentException("has not app with id = " + appId);
	}
	
	@Override
	public EzyAppContext getAppContext(String appName) {
		if(appContextsByName.containsKey(appName))
			return appContextsByName.get(appName);
		throw new IllegalArgumentException("has not app with name = " + appName);
	}
	
	@Override
	public EzyPluginContext getPluginContext(int pluginId) {
		if(pluginContextsById.containsKey(pluginId))
			return pluginContextsById.get(pluginId);
		throw new IllegalArgumentException("has not plugin with id = " + pluginId);
	}
	
	@Override
	public EzyPluginContext getPluginContext(String pluginName) {
		if(pluginContextsByName.containsKey(pluginName))
			return pluginContextsByName.get(pluginName);
		throw new IllegalArgumentException("has not plugin with name = " + pluginName);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzySendMessage.class, () -> new EzySendMessageImpl());
		suppliers.put(EzyFireEvent.class, () -> new EzyFireEventImpl(this));
	}
	
}
