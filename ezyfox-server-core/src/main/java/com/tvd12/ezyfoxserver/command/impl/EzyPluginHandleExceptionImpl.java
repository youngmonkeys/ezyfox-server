package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.EzyPlugin;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;

public class EzyPluginHandleExceptionImpl 
        extends EzyAbstractCommand 
        implements EzyHandleException {

    private EzyPlugin plugin;
    private EzyExceptionHandlersFetcher fetcher; 
    
    public EzyPluginHandleExceptionImpl(EzyPlugin plugin) {
        this.plugin = plugin;
        this.fetcher = (EzyExceptionHandlersFetcher) plugin;
    }
    
    @Override
    public void handle(Thread thread, Throwable throwable) {
        String pluginName = plugin.getSetting().getName();
        EzyExceptionHandlers handlers = fetcher.getExceptionHandlers();
        try {
            handlers.handleException(thread, throwable);
        }
        catch(Exception e) {
            getLogger().warn("handle exception on plugin " + pluginName + " error", e);
        }
        finally {
            getLogger().debug("handle plugin " + pluginName + " error", throwable);
        }
    }
    
}
