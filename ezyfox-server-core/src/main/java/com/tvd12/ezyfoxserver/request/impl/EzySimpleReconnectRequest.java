package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyReconnectParams;
import com.tvd12.ezyfoxserver.request.EzyReconnectRequest;

import lombok.Getter;

@Getter
public class EzySimpleReconnectRequest
        extends EzySimpleRequest<EzyReconnectParams>
        implements EzyReconnectRequest {
    
    protected EzySession oldSession;
    
    protected EzySimpleReconnectRequest(Builder builder) {
        super(builder);
        this.oldSession = builder.oldSession;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
        extends EzySimpleRequest.Builder<EzyReconnectParams, Builder> {
        
        protected EzySession oldSession;
        
        public Builder oldSession(EzySession oldSession) {
            this.oldSession = oldSession;
            return this;
        }
        
        @Override
        public EzyReconnectRequest build() {
            return new EzySimpleReconnectRequest(this);
        }
        
    }
    
}
