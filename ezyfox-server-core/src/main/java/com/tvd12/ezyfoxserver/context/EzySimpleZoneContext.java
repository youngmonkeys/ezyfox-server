package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.EzyComponent;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFireAppEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFireEventImpl;
import com.tvd12.ezyfoxserver.command.impl.EzyZoneFirePluginEventImpl;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

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
	protected EzyFireEvent fireEvent;
	protected EzyFireAppEvent fireAppEvent;
	protected EzyFirePluginEvent firePluginEvent;
	
	protected Map<String, EzyAppContext> appContextsByName = new ConcurrentHashMap<>();
	protected Map<String, EzyPluginContext> pluginContextsByName = new ConcurrentHashMap<>();

	@Override
	protected void init0() {
	    this.fireEvent = new EzyZoneFireEventImpl(this);
	    this.firePluginEvent = new EzyZoneFirePluginEventImpl(this);
	    this.fireAppEvent = new EzyZoneFireAppEventImpl(this);
	    this.properties.put(EzyFireEvent.class, fireEvent);
	    this.properties.put(EzyFirePluginEvent.class, firePluginEvent);
	    this.properties.put(EzyFireAppEvent.class, fireAppEvent);
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
    public void fireEvent(EzyConstant type, EzyEvent event) {
        fireEvent.fire(type, event);
    }
    
    @Override
    public void firePluginEvent(EzyConstant type, EzyEvent event) {
        firePluginEvent.fire(type, event);
    }
    
    @Override
    public void fireAppEvent(EzyConstant type, EzyEvent event) {
        fireAppEvent.fire(type, event);
    }
    
    @Override
    public void fireAppEvent(EzyConstant type, EzyEvent event, String username) {
        fireAppEvent.fire(type, event, username);
    }
    
    @Override
    public void fireAppEvent(EzyConstant type, EzyEvent event, EzyUser user) {
        fireAppEvent.fire(type, event, user);
    }
    
    @Override
    public void fireAppEvent(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter) {
        fireAppEvent.fire(type, event, filter);
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
	
	@Override
	protected EzyComponent getComponent() {
	    return (EzyComponent) parent.getServer();
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
	    this.fireEvent = null;
	    this.fireAppEvent = null;
	    this.firePluginEvent = null;
	    this.appContextsByName.clear();
        this.appContextsByName = null;
	    this.pluginContextsByName.clear();
	    this.pluginContextsByName = null;
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
