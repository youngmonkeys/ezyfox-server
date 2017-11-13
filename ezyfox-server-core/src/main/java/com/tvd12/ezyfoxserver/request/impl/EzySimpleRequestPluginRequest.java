package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyRequestPluginParams;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginRequest;

public class EzySimpleRequestPluginRequest
        extends EzySimpleUserRequest<EzyRequestPluginParams>
        implements EzyRequestPluginRequest {
    
    protected EzySimpleRequestPluginRequest(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleUserRequest.Builder<EzyRequestPluginParams, Builder> {
        
        @Override
        public EzyRequestPluginRequest build() {
            return new EzySimpleRequestPluginRequest(this);
        }
    }
    
}
