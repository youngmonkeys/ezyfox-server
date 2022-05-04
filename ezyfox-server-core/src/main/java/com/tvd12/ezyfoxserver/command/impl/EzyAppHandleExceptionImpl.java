package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyHandleException;

import static com.tvd12.ezyfox.io.EzyStrings.exceptionToSimpleString;

public class EzyAppHandleExceptionImpl
    extends EzyAbstractCommand
    implements EzyHandleException {

    private final EzyApplication app;
    private final EzyExceptionHandlersFetcher fetcher;

    public EzyAppHandleExceptionImpl(EzyApplication app) {
        this.app = app;
        this.fetcher = (EzyExceptionHandlersFetcher) app;
    }

    @Override
    public void handle(Thread thread, Throwable throwable) {
        String appName = app.getSetting().getName();
        EzyExceptionHandlers handlers = fetcher.getExceptionHandlers();
        if (handlers.isEmpty()) {
            logger.info("app: {} has no handler for exception:", appName, throwable);
        } else {
            try {
                handlers.handleException(thread, throwable);
            } catch (Exception e) {
                logger.warn(
                    "handle exception: {} on app: {} error",
                    exceptionToSimpleString(throwable),
                    appName,
                    e
                );
            }
        }
    }
}
