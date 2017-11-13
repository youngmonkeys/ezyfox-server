package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyHandShakeParams;
import com.tvd12.ezyfoxserver.request.EzyHandShakeRequest;

public class EzySimpleHandShakeRequest
        extends EzySimpleRequest<EzyHandShakeParams>
        implements EzyHandShakeRequest {

    protected EzySimpleHandShakeRequest(Builder builder) {
        super(builder);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleRequest.Builder<EzyHandShakeParams, Builder> {
        
        @Override
        public EzyHandShakeRequest build() {
            return new EzySimpleHandShakeRequest(this);
        }
    }
    
}
