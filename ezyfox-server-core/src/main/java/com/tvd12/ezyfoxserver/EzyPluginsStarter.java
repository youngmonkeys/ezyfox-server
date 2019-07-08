package com.tvd12.ezyfoxserver;

import java.util.Set;

import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntryLoader;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public class EzyPluginsStarter extends EzyZoneComponentsStater {

    protected EzyPluginsStarter(Builder builder) {
        super(builder);
    }
    
    @Override
    public void start() {
        startAllPlugins();
    }
    
    protected void startAllPlugins() {
        Set<String> pluginNames = getPluginNames();
        logger.info("start plugins: {}", pluginNames);
        pluginNames.forEach(this::startPlugin);
    }
    
    protected void startPlugin(String pluginName) {
        try {
            logger.debug("plugin: {} loading...", pluginName);
            EzyPluginContext context = zoneContext.getPluginContext(pluginName);
            EzyPlugin plugin = context.getPlugin();
            EzyPluginEntry entry = startPlugin(pluginName, newPluginEntryLoader(pluginName));
            ((EzyEntryAware)plugin).setEntry(entry);
            logger.debug("plugin: {} loaded", pluginName);
        }
        catch(Exception e) {
            logger.error("can not start plugin: {}", pluginName, e);
        } 
    }
    
    protected EzyPluginEntry startPlugin(String pluginName, EzyPluginEntryLoader loader) 
            throws Exception {
        EzyPluginEntry entry = loader.load();
        entry.config(getPluginContext(pluginName));
        entry.start();
        return entry;
    }
    
    protected Set<String> getPluginNames() {
        return zoneSetting.getPluginNames();
    }
    
    protected EzyPluginSetting getPluginByName(String name) {
        return zoneSetting.getPluginByName(name);
    }
    
    protected Class<EzyPluginEntryLoader> 
            getPluginEntryLoaderClass(String pluginName) throws Exception {
        return getPluginEntryLoaderClass(getPluginByName(pluginName));
    }
    
    @SuppressWarnings("unchecked")
    protected Class<EzyPluginEntryLoader> 
            getPluginEntryLoaderClass(EzyPluginSetting plugin) throws Exception {
        return (Class<EzyPluginEntryLoader>) Class.forName(plugin.getEntryLoader());
    }
    
    protected EzyPluginEntryLoader newPluginEntryLoader(String pluginName) throws Exception {
        return getPluginEntryLoaderClass(pluginName).newInstance();
    }
    
    protected EzyPluginContext getPluginContext(String pluginName) {
        return zoneContext.getPluginContext(pluginName);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyZoneComponentsStater.Builder<EzyPluginsStarter, Builder> {
        @Override
        public EzyPluginsStarter build() {
            return new EzyPluginsStarter(this);
        }
    }
    
}
