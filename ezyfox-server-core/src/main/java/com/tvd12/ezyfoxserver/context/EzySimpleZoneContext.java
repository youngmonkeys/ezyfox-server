package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyBroadcastAppsEvent;
import com.tvd12.ezyfoxserver.command.EzyBroadcastEvent;
import com.tvd12.ezyfoxserver.command.EzyBroadcastPluginsEvent;
import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastAppsEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyBroadcastPluginsEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneBroadcastEventImpl;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleZoneContext 
        extends EzyAbstractComplexContext 
        implements EzyZoneContext, EzyChildContext {

	@Getter
	protected EzyZone zone;
	
	@Setter
    @Getter
	protected EzyServerContext parent;
	protected EzyBroadcastEvent broadcastEvent;
	protected EzyBroadcastAppsEvent broadcastAppsEvent;
	protected EzyBroadcastPluginsEvent broadcastPluginsEvent;
	
	protected final Map<String, EzyAppContext> appContextsByName = new ConcurrentHashMap<>();
	protected final Map<String, EzyPluginContext> pluginContextsByName = new ConcurrentHashMap<>();

	@Override
	protected void init0() {
	    this.broadcastEvent = new EzyZoneBroadcastEventImpl(this);
	    this.broadcastAppsEvent = new EzyBroadcastAppsEventImpl(this);
	    this.broadcastPluginsEvent = new EzyBroadcastPluginsEventImpl(this);
	    this.properties.put(EzyBroadcastEvent.class, broadcastEvent);
	    this.properties.put(EzyBroadcastAppsEvent.class, broadcastAppsEvent);
	    this.properties.put(EzyBroadcastPluginsEvent.class, broadcastPluginsEvent);
	}
	
	@Override
    public <T> T get(Class<T> clazz) {
	    T property = getProperty(clazz);
        if(property != null)
            return property;
        return parent.get(clazz);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> T cmd(Class<T> clazz) {
        Supplier supplier = commandSuppliers.get(clazz);
        if(supplier != null)
            return (T) supplier.get();
        return parent.cmd(clazz);
    }
    
    @Override
    public void broadcast(EzyConstant eventType, EzyEvent event, boolean catchExeption) {
        broadcastEvent.fire(eventType, event, catchExeption);
    }
    
    @Override
    public void broadcastPlugins(EzyConstant type, EzyEvent event, boolean catchException) {
        broadcastPluginsEvent.fire(type, event, catchException);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, boolean catchException) {
        broadcastAppsEvent.fire(type, event, catchException);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, String username, boolean catchException) {
        broadcastAppsEvent.fire(type, event, username, catchException);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, EzyUser user, boolean catchException) {
        broadcastAppsEvent.fire(type, event, user, catchException);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter, boolean catchException) {
        broadcastAppsEvent.fire(type, event, filter, catchException);
    }
	
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
	    EzyAppContext appContext = appContextsByName.get(appName);
        if(appContext != null)
            return appContext;
        throw new IllegalArgumentException("has not app with name = " + appName);
    }
	
	@Override
    public EzyPluginContext getPluginContext(String pluginName) {
	    EzyPluginContext pluginContext = pluginContextsByName.get(pluginName);
        if(pluginContext != null)
            return pluginContext;
        throw new IllegalArgumentException("has not plugin with name = " + pluginName);
    }
	
	public void setZone(EzyZone zone) {
        this.zone = zone;
        this.component = (EzyComponent)zone;
    }
	
	@Override
	public void send(
	        EzyResponse response, 
	        EzySession recipient,
	        boolean encrypted,
	        EzyTransportType transportType) {
	    parent.send(response, recipient, encrypted, transportType);
	}
	
	@Override
	public void send(
	        EzyResponse response, 
	        Collection<EzySession> recipients, 
	        boolean encrypted,
	        EzyTransportType transportType) {
	    parent.send(response, recipients, encrypted, transportType);
	}
	
	@Override
	public void stream(
	        byte[] bytes, 
	        EzySession recipient, EzyTransportType transportType) {
	    parent.stream(bytes, recipient, transportType);
	}
	
	@Override
	public void stream(
	        byte[] bytes, 
	        Collection<EzySession> recipients, EzyTransportType transportType) {
	    parent.stream(bytes, recipients, transportType);
	}
	
	@Override
	protected void destroyComponents() {
	    destroyAppContexts();
        destroyPluginContexts();
        destroyZone();
	}
	
	@Override
	protected void clearProperties() {
	    super.clearProperties();
	    this.zone = null;
	    this.parent = null;
	    this.broadcastEvent = null;
	    this.broadcastAppsEvent = null;
	    this.broadcastPluginsEvent = null;
	    this.appContextsByName.clear();
	    this.pluginContextsByName.clear();
	}
	
	private void destroyAppContexts() {
        for(EzyAppContext ac : appContexts)
            this.destroyAppContext(ac);
    }
    
    private void destroyPluginContexts() {
        for(EzyPluginContext pc : pluginContexts)
            this.destroyPluginContext(pc);
    }
    
    private void destroyZone() {
        processWithLogException(() -> ((EzyDestroyable)zone).destroy());
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
        return new EzyHashCodes().append(zone).toHashCode();
    }
    
    @Override
    protected void preDestroy() {
        logger.debug("destroy ZoneContext({})", zone);
    }
	
}
