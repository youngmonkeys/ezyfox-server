package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyReconnectParams;
import com.tvd12.ezyfoxserver.request.EzyReconnectRequest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleReconnectRequest
        extends EzySimpleRequest<EzyReconnectParams>
        implements EzyReconnectRequest {
    
    protected EzySession oldSession;

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
            return (EzyReconnectRequest) super.build();
        }
        
        @Override
        protected EzySimpleReconnectRequest newProduct() {
            EzySimpleReconnectRequest answer = new EzySimpleReconnectRequest();
            answer.setOldSession(oldSession);
            return answer;
        }
    }
    
}
