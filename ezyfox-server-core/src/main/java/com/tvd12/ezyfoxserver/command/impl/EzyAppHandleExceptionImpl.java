package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyHandleException;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;

public class EzyAppHandleExceptionImpl 
        extends EzyAbstractCommand 
        implements EzyHandleException {

    private EzyApplication app;
    private EzyExceptionHandlersFetcher fetcher; 
    
    public EzyAppHandleExceptionImpl(EzyApplication app) {
        this.app = app;
        this.fetcher = (EzyExceptionHandlersFetcher) app;
    }
    
    @Override
    public void handle(Thread thread, Throwable throwable) {
        String appName = app.getSetting().getName();
        EzyExceptionHandlers handlers = fetcher.getExceptionHandlers();
        try {
            handlers.handleException(thread, throwable);
        }
        catch(Exception e) {
            getLogger().warn("handle exception on app " + appName + " error", e);
        }
        finally {
            getLogger().debug("handle app " + appName + " error", throwable);
        }
    }
    
}
