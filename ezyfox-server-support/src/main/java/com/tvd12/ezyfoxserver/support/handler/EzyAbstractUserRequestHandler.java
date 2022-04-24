package com.tvd12.ezyfoxserver.support.handler;

import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyContext;

public abstract class EzyAbstractUserRequestHandler<C extends EzyContext, D>
        extends EzyLoggable
        implements EzyUserRequestHandler<C, D> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<D> getDataType() {
        try {
            return EzyGenerics.getTwoGenericClassArguments(getClass().getGenericSuperclass())[1];
        }
        catch (Exception e) {
            return null;
        }
    }

}
