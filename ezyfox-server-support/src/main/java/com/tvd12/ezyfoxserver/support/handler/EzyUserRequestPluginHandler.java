package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;

public interface EzyUserRequestPluginHandler<D>
    extends EzyUserRequestHandler<EzyPluginContext, D> {

    @SuppressWarnings("unchecked")
    @Override
    default Class<D> getDataType() {
        try {
            return EzyGenerics.getGenericInterfacesArguments(
                getClass(),
                EzyUserRequestPluginHandler.class, 1)[0];
        } catch (Exception e) {
            return null;
        }
    }
}
