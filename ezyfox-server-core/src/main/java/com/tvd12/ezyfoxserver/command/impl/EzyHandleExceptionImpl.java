package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyHandleException;

import static com.tvd12.ezyfox.io.EzyStrings.exceptionToSimpleString;

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
        if (handlers.isEmpty()) {
            logger.info("there is no handler for exception: ", throwable);
        } else {
            try {
                handlers.handleException(thread, throwable);
            } catch (Exception e) {
                logger.warn(
                    "handle exception: {} error",
                    exceptionToSimpleString(throwable),
                    e
                );
            }
        }
    }
}
