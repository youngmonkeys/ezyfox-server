package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyDisconnectUser;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.impl.EzyDisconnectUserImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFireAppEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFirePluginEventImpl;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleZoneContext 
        extends EzyAbstractComplexContext 
        implements EzyZoneContext, EzyChildContext {

	@Setter
	@Getter
	protected EzyZone zone;
	
	@Setter
    @Getter
	protected EzyServerContext parent;
	
	protected Map<String, EzyAppContext> appContextsByName = new ConcurrentHashMap<>();
	protected Map<String, EzyPluginContext> pluginContextsByName = new ConcurrentHashMap<>();

	@Override
	public void addAppContext(EzyAppSetting app, EzyAppContext appContext) {
	    super.addAppContext(app, appContext);
	    appContextsByName.put(app.getName(), appContext);
	}
	
	@Override
	public void addPluginContext(EzyPluginSetting plugin, EzyPluginContext pluginContext) {
	    super.addPluginContext(plugin, pluginContext);
	    pluginContextsByName.put(plugin.getName(), pluginContext);
	}
	
	@Override
    public EzyAppContext getAppContext(String appName) {
        if(appContextsByName.containsKey(appName))
            return appContextsByName.get(appName);
        throw new IllegalArgumentException("has not app with name = " + appName);
    }
	
	@Override
    public EzyPluginContext getPluginContext(String pluginName) {
        if(pluginContextsByName.containsKey(pluginName))
            return pluginContextsByName.get(pluginName);
        throw new IllegalArgumentException("has not plugin with name = " + pluginName);
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
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addCommandSuppliers(Map<Class, Supplier> suppliers) {
		super.addCommandSuppliers(suppliers);
		suppliers.put(EzyFireEvent.class, () -> new EzyZoneFireEventImpl(this));
		suppliers.put(EzyFirePluginEvent.class, () -> new EzyZoneFirePluginEventImpl(this));
		suppliers.put(EzyFireAppEvent.class, ()-> new EzyZoneFireAppEventImpl(this));
		suppliers.put(EzyDisconnectUser.class, ()-> new EzyDisconnectUserImpl(this));
	}
	
	@Override
	protected EzyExceptionHandlersFetcher getExceptionHandlersFetcher() {
	    return (EzyExceptionHandlersFetcher) parent.getServer();
	}
	
	@Override
	public void destroy() {
	    destroyZone();
	    destroyAppContexts();
	    destroyPluginContexts();
	}
	
	private void destroyZone() {
	    processWithLogException(() -> ((EzyDestroyable)zone).destroy());
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
	
	@Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleZoneContext>()
                .function(t -> t.zone)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(zone).hashCode();
    }
	
}
