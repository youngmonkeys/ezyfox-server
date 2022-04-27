package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

@Setter
@EzyPrototype
@EzyRequestListener("noDataBinding")
public class ClientNoDataBindingRequestHandler
    extends EzyLoggable
    implements
    EzyHandler {

    @Override
    public void handle() {
    }
}
