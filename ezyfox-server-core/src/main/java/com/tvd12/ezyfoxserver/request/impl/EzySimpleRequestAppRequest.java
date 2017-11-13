package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;

public class EzySimpleRequestAppRequest
        extends EzySimpleUserRequest<EzyRequestAppParams>
        implements EzyRequestAppRequest {
    
    protected EzySimpleRequestAppRequest(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleUserRequest.Builder<EzyRequestAppParams, Builder> {
        
        @Override
        public EzyRequestAppRequest build() {
            return new EzySimpleRequestAppRequest(this);
        }
    }
    
}
