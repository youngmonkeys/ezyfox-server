package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyEntityBuilders;

public class EzySimpleResponseParams
    extends EzyEntityBuilders
    implements EzyResponseParams {
    private static final long serialVersionUID = 6981031463211413962L;

    @Override
    public final EzyArray serialize() {
        return doSerialize().build();
    }

    protected EzyArrayBuilder doSerialize() {
        return newArrayBuilder();
    }

    @Override
    public void release() {}
}
