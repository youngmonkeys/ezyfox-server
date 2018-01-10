package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyArrayDeserializable;

public class EzySimpleRequestParams implements EzyRequestParams, EzyArrayDeserializable {
    private static final long serialVersionUID = -2484867616935892598L;

    @Override
    public void deserialize(EzyArray t) {
    }
    
    @Override
    public void release() {
    }
}
