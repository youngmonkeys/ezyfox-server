package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.request.EzyRequestAppRequest;

public class EzySimpleRequestAppRequest
        extends EzySimpleUserRequest<EzyRequestAppParams>
        implements EzyRequestAppRequest {

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleUserRequest.Builder<EzyRequestAppParams, Builder> {
        
        @Override
        public EzyRequestAppRequest build() {
            return (EzyRequestAppRequest) super.build();
        }
        
        @Override
        protected EzySimpleRequestAppRequest newProduct() {
            return new EzySimpleRequestAppRequest();
        }
    }
    
}
