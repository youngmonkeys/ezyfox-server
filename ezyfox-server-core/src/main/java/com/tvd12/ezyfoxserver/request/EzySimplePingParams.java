package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyArrayDeserializable;

import lombok.Getter;

@Getter
public class EzySimplePingParams implements EzyPingParams, EzyArrayDeserializable {
    private static final long serialVersionUID = 4329468610224689234L;
    
    @Override
    public void deserialize(EzyArray t) {
    }
    
}
