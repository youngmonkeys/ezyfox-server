package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;
import com.tvd12.ezyfoxserver.request.EzyAccessAppRequest;

public class EzySimpleAccessAppRequest
        extends EzySimpleUserRequest<EzyAccessAppParams>
        implements EzyAccessAppRequest {
    
    protected EzySimpleAccessAppRequest(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleUserRequest.Builder<EzyAccessAppParams, Builder> {
        
        @Override
        public EzyAccessAppRequest build() {
            return new EzySimpleAccessAppRequest(this);
        }
    }
    
}
