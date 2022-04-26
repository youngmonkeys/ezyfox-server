package com.tvd12.ezyfoxserver.socket;

import lombok.Setter;

public abstract class EzySimpleSocketEventLoop extends EzySocketEventLoop {

    @Setter
    protected String threadName;
    @Setter
    protected int threadPoolSize;

    @Override
    protected String threadName() {
        return threadName;
    }

    @Override
    protected int threadPoolSize() {
        return threadPoolSize;
    }

    protected final void eventLoop() {
        logger.debug("{} event loop has started", currentThreadName());
        doEventLoop();
        logger.debug("{} event loop has stopped", currentThreadName());
    }

    protected abstract void doEventLoop();

    private String currentThreadName() {
        return Thread.currentThread().getName();
    }
}
