package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyPingParams;
import com.tvd12.ezyfoxserver.request.EzyPingRequest;

public class EzySimplePingRequest
        extends EzySimpleRequest<EzyPingParams>
        implements EzyPingRequest {
    
    protected EzySimplePingRequest(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleRequest.Builder<EzyPingParams, Builder> {
        
        @Override
        public EzyPingRequest build() {
            return new EzySimplePingRequest(this);
        }
    }
    
}
