package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyAppContextAware;

import lombok.Setter;

@Setter
@EzyRequestListener("app")
public class ClientAppRequestHandler
        extends EzyLoggable
        implements
            EzyAppContextAware,
            EzyHandler {

    protected EzyAppContext appContext;

    @Override
    public void handle() {
    }
}
