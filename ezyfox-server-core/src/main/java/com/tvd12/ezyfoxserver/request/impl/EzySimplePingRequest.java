package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyPingParams;
import com.tvd12.ezyfoxserver.request.EzyPingRequest;

public class EzySimplePingRequest
        extends EzySimpleRequest<EzyPingParams>
        implements EzyPingRequest {

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleRequest.Builder<EzyPingParams, Builder> {
        
        @Override
        public EzyPingRequest build() {
            return (EzyPingRequest) super.build();
        }
        
        @Override
        protected EzySimplePingRequest newProduct() {
            return new EzySimplePingRequest();
        }
    }
    
}
