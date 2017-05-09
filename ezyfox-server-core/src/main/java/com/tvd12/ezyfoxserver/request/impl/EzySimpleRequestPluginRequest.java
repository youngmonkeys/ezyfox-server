package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.request.EzyRequestPluginParams;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginRequest;

public class EzySimpleRequestPluginRequest
        extends EzySimpleUserRequest<EzyRequestPluginParams>
        implements EzyRequestPluginRequest {

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleUserRequest.Builder<EzyRequestPluginParams, Builder> {
        
        @Override
        public EzyRequestPluginRequest build() {
            return (EzyRequestPluginRequest) super.build();
        }
        
        @Override
        protected EzySimpleRequestPluginRequest newProduct() {
            return new EzySimpleRequestPluginRequest();
        }
    }
    
}
