package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.request.EzyHandShakeParams;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzySimpleHandShakeParams implements EzyHandShakeParams {

    protected String clientId;
    protected String clientKey;
    protected String clientType;
    protected String clientVersion;
    protected String reconnectToken;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyHandShakeParams> {
        
        protected String clientId;
        protected String clientKey;
        protected String clientType;
        protected String clientVersion;
        protected String reconnectToken;
        
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public Builder clientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }
        
        public Builder clientType(String clientType) {
            this.clientType = clientType;
            return this;
        }
        
        public Builder clientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
            return this;
        }
        
        public Builder reconnectToken(String reconnectToken) {
            this.reconnectToken = reconnectToken;
            return this;
        }
        
        @Override
        public EzyHandShakeParams build() {
            EzySimpleHandShakeParams answer = new EzySimpleHandShakeParams();
            answer.setClientId(clientId);
            answer.setClientKey(clientKey);
            answer.setClientType(clientType);
            answer.setClientVersion(clientVersion);
            answer.setReconnectToken(reconnectToken);
            return answer;
        }
    }
    
}
