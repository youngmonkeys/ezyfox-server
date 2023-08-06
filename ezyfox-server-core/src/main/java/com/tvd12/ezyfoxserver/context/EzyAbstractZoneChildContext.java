package com.tvd12.ezyfoxserver.context;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledExecutorService;

@Setter
@Getter
public abstract class EzyAbstractZoneChildContext
    extends EzyAbstractContext {

    protected EzyZoneContext parent;
    protected ScheduledExecutorService executorService;

    @Override
    protected <T> T parentGet(Class<T> clazz) {
        return parent.get(clazz);
    }

    @Override
    protected <T> T parentCmd(Class<T> clazz) {
        return parent.cmd(clazz);
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
        this.properties.put(ScheduledExecutorService.class, executorService);
    }
}
