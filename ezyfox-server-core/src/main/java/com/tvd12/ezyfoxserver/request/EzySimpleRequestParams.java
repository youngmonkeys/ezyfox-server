package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

public class EzySimpleRequestParams implements EzyRequestParams {
    private static final long serialVersionUID = -2484867616935892598L;

    public static final EzySimpleRequestParams EMPTY = new EzySimpleRequestParams();
    
    @Override
    public void deserialize(EzyArray t) {
    }
    
    @Override
    public void release() {
    }
}
