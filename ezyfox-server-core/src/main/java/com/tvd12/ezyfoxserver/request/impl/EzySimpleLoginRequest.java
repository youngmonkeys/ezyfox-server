package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyLoginParams;
import com.tvd12.ezyfoxserver.request.EzyLoginRequest;

public class EzySimpleLoginRequest
        extends EzySimpleRequest<EzyLoginParams>
        implements EzyLoginRequest {

    protected EzySimpleLoginRequest(Builder builder) {
        super(builder);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleRequest.Builder<EzyLoginParams, Builder> {
        
        @Override
        public EzyLoginRequest build() {
            return new EzySimpleLoginRequest(this);
        }
    }
    
}
