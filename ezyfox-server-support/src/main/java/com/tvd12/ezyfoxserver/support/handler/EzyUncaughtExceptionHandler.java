package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

public interface EzyUncaughtExceptionHandler<C extends EzyContext, D> {

    void handleException(
        C context,
        EzyUserSessionEvent event,
        String command,
        D data,
        Exception exception
    ) throws Exception;
}
