package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EzyAbstractComplexContext
    extends EzyAbstractContext
    implements EzyComplexContext {

    protected final Set<EzyAppContext> appContexts
        = new HashSet<>();
    protected final Map<Integer, EzyAppContext> appContextsById
        = new ConcurrentHashMap<>();
    protected final Set<EzyPluginContext> pluginContexts
        = new HashSet<>();
    protected final Map<Integer, EzyPluginContext> pluginContextsById
        = new ConcurrentHashMap<>();

    public void addAppContext(EzyAppSetting app, EzyAppContext appContext) {
        appContexts.add(appContext);
        appContextsById.put(app.getId(), appContext);
    }

    public void addAppContexts(Collection<EzyAppContext> appContexts) {
        for (EzyAppContext ctx : appContexts) {
            addAppContext(ctx.getApp().getSetting(), ctx);
        }
    }

    public void addPluginContext(EzyPluginSetting plugin, EzyPluginContext pluginContext) {
        pluginContexts.add(pluginContext);
        pluginContextsById.put(plugin.getId(), pluginContext);
    }

    public void addPluginContexts(Collection<EzyPluginContext> pluginContexts) {
        for (EzyPluginContext ctx : pluginContexts) {
            addPluginContext(ctx.getPlugin().getSetting(), ctx);
        }
    }

    @Override
    public EzyAppContext getAppContext(int appId) {
        EzyAppContext appContext = appContextsById.get(appId);
        if (appContext != null) {
            return appContext;
        }
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
        EzyPluginContext pluginContext = pluginContextsById.get(pluginId);
        if (pluginContext != null) {
            return pluginContext;
        }
        throw new IllegalArgumentException("has not plugin with id = " + pluginId);
    }

    @Override
    public final void destroy() {
        super.destroy();
        destroyComponents();
        clearProperties();
        postDestroy();
    }

    protected void clearProperties() {
        this.appContexts.clear();
        this.pluginContexts.clear();
        this.appContextsById.clear();
        this.pluginContextsById.clear();
    }

    protected void destroyComponents() {}
}
