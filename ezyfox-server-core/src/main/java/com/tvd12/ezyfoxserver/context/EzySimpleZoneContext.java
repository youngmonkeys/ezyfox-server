package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

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
        if(containsKey(clazz))
            return getProperty(clazz);
        return parent.get(clazz);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T cmd(Class<T> clazz) {
        if(commandSuppliers.containsKey(clazz))
            return (T) commandSuppliers.get(clazz).get();
        return parent.cmd(clazz);
    }
    
    @Override
    public void broadcast(EzyConstant eventType, EzyEvent event) {
        broadcastEvent.fire(eventType, event);
    }
    
    @Override
    public void broadcastPlugins(EzyConstant type, EzyEvent event) {
        broadcastPluginsEvent.fire(type, event);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event) {
        broadcastAppsEvent.fire(type, event);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, String username) {
        broadcastAppsEvent.fire(type, event, username);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, EzyUser user) {
        broadcastAppsEvent.fire(type, event, user);
    }
    
    @Override
    public void broadcastApps(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter) {
        broadcastAppsEvent.fire(type, event, filter);
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
	
	public void setZone(EzyZone zone) {
        this.zone = zone;
        this.component = (EzyComponent)zone;
    }
	
	@Override
	public void send(EzyResponse response, EzySession recipient, boolean immediate) {
	    parent.send(response, recipient, immediate);
	}
	
	@Override
	public void send(EzyResponse response, Collection<EzySession> recipients, boolean immediate) {
	    parent.send(response, recipients, immediate);
	}
	
	@Override
	public void stream(byte[] bytes, EzySession recipient) {
	    parent.stream(bytes, recipient);
	}
	
	@Override
	public void stream(byte[] bytes, Collection<EzySession> recipients) {
	    parent.stream(bytes, recipients);
	}
	
	@Override
	public void destroy() {
	    super.destroy();
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
	
	private void destroyZone() {
	    processWithLogException(() -> ((EzyDestroyable)zone).destroy());
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
