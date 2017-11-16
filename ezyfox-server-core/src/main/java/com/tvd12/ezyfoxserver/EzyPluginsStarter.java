package com.tvd12.ezyfoxserver;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public class EzyPluginsStarter extends EzyComponentsStater {

    protected EzyPluginsStarter(Builder builder) {
        super(builder);
    }
    
    @Override
    public void start() {
        startAllPlugins();
    }
    
    protected void startAllPlugins() {
        Set<String> pluginNames = getPluginNames();
        getLogger().info("start plugins: {}", pluginNames);
        pluginNames.forEach(this::startPlugin);
    }
    
    protected void startPlugin(String pluginName) {
        try {
            getLogger().debug("plugin " + pluginName + " loading...");
            EzyPluginContext context = serverContext.getPluginContext(pluginName);
            EzyPlugin plugin = context.getPlugin();
            EzyPluginEntry entry = startPlugin(pluginName, newPluginEntryLoader(pluginName));
            ((EzyEntryAware)plugin).setEntry(entry);
            getLogger().debug("plugin " + pluginName + " loaded");
        }
        catch(Exception e) {
            getLogger().error("can not start plugin " + pluginName, e);
        } 
    }
    
    protected EzyPluginEntry startPlugin(String pluginName, EzyPluginEntryLoader loader) 
            throws Exception {
        EzyPluginEntry entry = loader.load();
        entry.config(getPluginContext(pluginName));
        entry.start();
        return entry;
    }
    
    @JsonIgnore
    public Set<String> getPluginNames() {
        return settings.getPluginNames();
    }
    
    public EzyPluginSetting getPluginByName(String name) {
        return settings.getPluginByName(name);
    }
    
    public Class<EzyPluginEntryLoader> 
            getPluginEntryLoaderClass(String pluginName) throws Exception {
        return getPluginEntryLoaderClass(getPluginByName(pluginName));
    }
    
    @SuppressWarnings("unchecked")
    public Class<EzyPluginEntryLoader> 
            getPluginEntryLoaderClass(EzyPluginSetting plugin) throws Exception {
        return (Class<EzyPluginEntryLoader>) Class.forName(plugin.getEntryLoader());
    }
    
    public EzyPluginEntryLoader newPluginEntryLoader(String pluginName) throws Exception {
        return getPluginEntryLoaderClass(pluginName).newInstance();
    }
    
    protected EzyPluginContext getPluginContext(String pluginName) {
        return serverContext.getPluginContext(pluginName);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyComponentsStater.Builder<EzyPluginsStarter, Builder> {
        @Override
        public EzyPluginsStarter build() {
            return new EzyPluginsStarter(this);
        }
    }
    
}
