package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.command.EzyHandleException;

public final class EzyPluginContexts {

    private EzyPluginContexts() {
    }
    
    public static void handleException(EzyPluginContext context, Thread thread, Throwable throwable) {
        context.get(EzyHandleException.class).handle(thread, throwable);
    }
    
}
