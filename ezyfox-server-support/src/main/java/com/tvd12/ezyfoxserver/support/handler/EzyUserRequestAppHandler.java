package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfoxserver.context.EzyAppContext;

public interface EzyUserRequestAppHandler<D>
    extends EzyUserRequestHandler<EzyAppContext, D> {

    @SuppressWarnings("unchecked")
    @Override
    default Class<D> getDataType() {
        try {
            return EzyGenerics.getGenericInterfacesArguments(
                getClass(),
                EzyUserRequestAppHandler.class, 1)[0];
        } catch (Exception e) {
            return null;
        }
    }
}
