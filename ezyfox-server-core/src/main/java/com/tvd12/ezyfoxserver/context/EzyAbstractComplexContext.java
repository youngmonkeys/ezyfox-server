package com.tvd12.ezyfoxserver.context;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public abstract class EzyAbstractComplexContext 
        extends EzyAbstractContext 
        implements EzyComplexContext {

    protected final Set<EzyAppContext> appContexts = new HashSet<>();
    protected final Map<Integer, EzyAppContext> appContextsById = new ConcurrentHashMap<>();
    protected final Set<EzyPluginContext> pluginContexts = new HashSet<>(); 
    protected final Map<Integer, EzyPluginContext> pluginContextsById = new ConcurrentHashMap<>();
    
    public void addAppContext(EzyAppSetting app, EzyAppContext appContext) {
        appContexts.add(appContext);
        appContextsById.put(app.getId(), appContext);
    }
    
    public void addAppContexts(Collection<EzyAppContext> appContexts) {
        for(EzyAppContext ctx : appContexts)
            addAppContext(ctx.getApp().getSetting(), ctx);
    }
    
    public void addPluginContext(EzyPluginSetting plugin, EzyPluginContext pluginContext) {
        pluginContexts.add(pluginContext);
        pluginContextsById.put(plugin.getId(), pluginContext);
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
    public Collection<EzyAppContext> getAppContexts() {
        return appContexts;
    }
    
    @Override
    public Collection<EzyPluginContext> getPluginContexts() {
        return pluginContexts;
    }
    
    @Override
    public EzyPluginContext getPluginContext(int pluginId) {
        if(pluginContextsById.containsKey(pluginId))
            return pluginContextsById.get(pluginId);
        throw new IllegalArgumentException("has not plugin with id = " + pluginId);
    }
    
    @Override
    public void destroy() {
        super.destroy();
        destroyAppContexts();
        destroyPluginContexts();
        clearProperties();
    }
    
    protected void clearProperties() {
        this.appContexts.clear();
        this.pluginContexts.clear();
        this.appContextsById.clear();
        this.pluginContextsById.clear();
    }
    
    private void destroyAppContexts() {
        appContexts.forEach(this::destroyAppContext);
    }
    
    private void destroyPluginContexts() {
        pluginContexts.forEach(this::destroyPluginContext);
    }
    
    private void destroyAppContext(EzyAppContext appContext) {
        processWithLogException(() -> ((EzyDestroyable)appContext).destroy());
    }
    
    private void destroyPluginContext(EzyPluginContext pluginContext) {
        processWithLogException(() -> ((EzyDestroyable)pluginContext).destroy());
    }
}
