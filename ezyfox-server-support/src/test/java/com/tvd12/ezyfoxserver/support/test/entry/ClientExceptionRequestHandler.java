package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

@Setter
@EzyPrototype
@EzyRequestListener("exception")
public class ClientExceptionRequestHandler
    extends EzyLoggable
    implements
    EzyHandler {

    @Override
    public void handle() {
        throw new IllegalStateException("server maintain");
    }
}
