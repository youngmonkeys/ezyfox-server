package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.request.EzyReconnectParams;

public class EzySimpleReconnectParams implements EzyReconnectParams {
    
    protected EzySimpleReconnectParams(Builder builder) {
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyReconnectParams> {
        @Override
        public EzyReconnectParams build() {
            return new EzySimpleReconnectParams(this);
        }
    }
    
}
