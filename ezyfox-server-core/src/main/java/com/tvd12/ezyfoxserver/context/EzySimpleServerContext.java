package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyDisconnectSession;
import com.tvd12.ezyfoxserver.command.EzyDisconnectUser;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.command.impl.EzyDisconnectSessionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyDisconnectUserImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyFireAppEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyFirePluginEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendMessageImpl;
import com.tvd12.ezyfoxserver.command.impl.EzySendResponseImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyServerHandleExceptionImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyServerShutdown;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleServerContext extends EzyAbstractContext implements EzyServerContext {

	@Setter
	@Getter
	protected EzyServer server;
	
	protected Map<Integer, EzyAppContext> appContextsById = new ConcurrentHashMap<>();
	protected Map<String, EzyAppContext> appContextsByName = new ConcurrentHashMap<>();
	protected Map<Integer, EzyPluginContext> pluginContextsById = new ConcurrentHashMap<>();
	protected Map<String, EzyPluginContext> pluginContextsByName = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		if(commandSuppliers.containsKey(clazz))
			return (T) commandSuppliers.get(clazz).get();
		if(containsKey(clazz))
			return getProperty(clazz);
		throw new IllegalArgumentException("has no instance of " + clazz);
	}
	
	public void addAppContext(EzyAppSetting app, EzyAppContext appContext) {
		appContextsById.put(app.getId(), appContext);
		appContextsByName.put(app.getName(), appContext);
	}
	
	public void addAppContexts(Collection<EzyAppContext> appContexts) {
		for(EzyAppContext ctx : appContexts)
			addAppContext(ctx.getApp().getSetting(), ctx);
	}
	
	public void addPluginContext(EzyPluginSetting plugin, EzyPluginContext pluginContext) {
		pluginContextsById.put(plugin.getId(), pluginContext);
		pluginContextsByName.put(plugin.getName(), pluginContext);
	}
	
	public void addPluginContexts(Collection<EzyPluginContext> pluginContexts) {
		for(EzyPluginContext ctx : pluginContexts)
			addPluginContext(ctx.getPlugin().getSetting(), ctx);
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
		suppliers.put(EzyShutdown.class, ()-> new EzyServerShutdown(this));
		suppliers.put(EzySendMessage.class, () -> new EzySendMessageImpl());
		suppliers.put(EzySendResponse.class, () -> new EzySendResponseImpl(this));
		suppliers.put(EzyFireEvent.class, () -> new EzyFireEventImpl(this));
		suppliers.put(EzyFirePluginEvent.class, () -> new EzyFirePluginEventImpl(this));
		suppliers.put(EzyFireAppEvent.class, ()-> new EzyFireAppEventImpl(this));
		suppliers.put(EzyDisconnectUser.class, ()-> new EzyDisconnectUserImpl(this));
		suppliers.put(EzyDisconnectSession.class, ()-> new EzyDisconnectSessionImpl(this));
		suppliers.put(EzyHandleException.class, ()-> new EzyServerHandleExceptionImpl(getServer()));
	}
	
	@Override
	protected EzyExceptionHandlersFetcher getExceptionHandlersFetcher() {
	    return (EzyExceptionHandlersFetcher) server;
	}
	
	@Override
	public void destroy() {
	    destroyServer();
	    destroyAppContexts();
	    destroyPluginContexts();
	}
	
	private void destroyServer() {
	    processWithLogException(() -> ((EzyDestroyable)server).destroy());
	}
	
	private void destroyAppContexts() {
        appContextsById.values().forEach(this::destroyAppContext);
    }
	
	private void destroyPluginContexts() {
	    pluginContextsById.values().forEach(this::destroyPluginContext);
	}
	
	private void destroyAppContext(EzyAppContext appContext) {
        processWithLogException(() -> ((EzyDestroyable)appContext).destroy());
    }
	
	private void destroyPluginContext(EzyPluginContext pluginContext) {
	    processWithLogException(() -> ((EzyDestroyable)pluginContext).destroy());
	}
	
}
