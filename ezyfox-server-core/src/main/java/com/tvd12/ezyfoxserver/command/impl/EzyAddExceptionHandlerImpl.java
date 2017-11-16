package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyAddExceptionHandler;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;

public class EzyAddExceptionHandlerImpl 
        extends EzyAbstractCommand
        implements EzyAddExceptionHandler {

    private EzyExceptionHandlersFetcher fetcher; 
    
    public EzyAddExceptionHandlerImpl(EzyExceptionHandlersFetcher fetcher) {
        this.fetcher = fetcher;
    }
    
    @Override
    public void add(EzyExceptionHandlers handler) {
        EzyExceptionHandlers handlers = fetcher.getExceptionHandlers();
        handlers.addExceptionHandler(handler);
    }
    
}
