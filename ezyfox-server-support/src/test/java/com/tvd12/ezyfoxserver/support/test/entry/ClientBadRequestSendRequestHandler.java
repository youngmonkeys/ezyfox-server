package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;

import lombok.Setter;

@Setter
@EzyPrototype
@EzyRequestListener("badRequestSend")
public class ClientBadRequestSendRequestHandler
        extends EzyLoggable
        implements
            EzyHandler {

    @Override
    public void handle() {
        throw new EzyBadRequestException(1, "test", true);
    }
}
