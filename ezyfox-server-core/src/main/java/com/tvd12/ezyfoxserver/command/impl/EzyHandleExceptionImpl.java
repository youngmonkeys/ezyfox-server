package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyHandleException;

public class EzyHandleExceptionImpl 
        extends EzyAbstractCommand 
        implements EzyHandleException {

    private final EzyExceptionHandlersFetcher fetcher; 
    
    public EzyHandleExceptionImpl(EzyExceptionHandlersFetcher fetcher) {
        this.fetcher = fetcher;
    }
    
    @Override
    public void handle(Thread thread, Throwable throwable) {
        EzyExceptionHandlers handlers = fetcher.getExceptionHandlers();
        try {
            handlers.handleException(thread, throwable);
        }
        catch(Exception e) {
            logger.error("handle exception error", e);
        }
        finally {
            logger.warn("handle exception", throwable);
        }
    }
    
}
