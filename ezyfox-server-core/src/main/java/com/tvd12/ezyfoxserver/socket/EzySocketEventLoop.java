package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.concurrent.EzyThreadList;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzySocketEventLoop
    extends EzyLoggable
    implements EzyStartable, EzyDestroyable {

    protected volatile boolean active;
    protected EzyThreadList threadPool;

    protected abstract String threadName();

    protected abstract int threadPoolSize();

    @Override
    public void start() throws Exception {
        initThreadPool();
        setActive(true);
        startLoopService();
    }

    protected void setActive(boolean value) {
        this.active = value;
    }

    private void startLoopService() {
        threadPool.execute();
    }

    private Runnable newServiceTask() {
        return this::eventLoop;
    }

    protected abstract void eventLoop();

    protected void initThreadPool() {
        threadPool = new EzyThreadList(
            threadPoolSize(),
            newServiceTask(),
            threadName()
        );
    }

    @Override
    public void destroy() {
        processWithLogException(this::doDestroy);
    }

    protected void doDestroy() {
        setActive(false);
        threadPool.interrupt();
    }
}
