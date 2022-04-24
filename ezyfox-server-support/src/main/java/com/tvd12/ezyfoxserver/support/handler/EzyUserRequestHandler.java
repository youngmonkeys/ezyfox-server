package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;

public interface EzyUserRequestHandler<C extends EzyContext, D> {

    void handle(C context, EzyUserSessionEvent event, D data);

    @SuppressWarnings("unchecked")
    default Class<D> getDataType() {
        try {
            return EzyGenerics.getGenericInterfacesArguments(
                    getClass(),
                    EzyUserRequestHandler.class, 2)[1];
        }
        catch (Exception e) {
            return null;
        }
    }
}
