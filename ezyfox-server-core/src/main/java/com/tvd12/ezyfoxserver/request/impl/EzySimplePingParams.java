package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.request.EzyPingParams;

import lombok.Getter;

@Getter
public class EzySimplePingParams implements EzyPingParams {
    
    protected EzySimplePingParams(Builder builder) {
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyPingParams> {
        @Override
        public EzyPingParams build() {
            return new EzySimplePingParams(this);
        }
    }
    
}
