package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

public class EzySimpleRequestParams implements EzyRequestParams {
    public static final EzySimpleRequestParams EMPTY = new EzySimpleRequestParams();
    private static final long serialVersionUID = -2484867616935892598L;

    @Override
    public void deserialize(EzyArray t) {
    }

    @Override
    public void release() {
    }
}
