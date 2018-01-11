package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;

@Getter
public class EzySimplePingParams
        extends EzySimpleRequestParams
        implements EzyPingParams {
    private static final long serialVersionUID = 4329468610224689234L;
    
    @Override
    public void deserialize(EzyArray t) {
    }
    
}
