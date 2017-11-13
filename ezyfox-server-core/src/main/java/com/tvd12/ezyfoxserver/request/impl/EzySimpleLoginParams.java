package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.request.EzyLoginParams;

import lombok.Getter;

@Getter
public class EzySimpleLoginParams implements EzyLoginParams {

    private EzyArray data;
    private String username;
    private String password;
    
    protected EzySimpleLoginParams(Builder builder) {
        this.data = builder.data;
        this.username = builder.username;
        this.password = builder.password;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyLoginParams> {
        
        private EzyArray data;
        private String username;
        private String password;
        
        public Builder data(EzyArray data) {
            this.data = data;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        
        @Override
        public EzyLoginParams build() {
            return new EzySimpleLoginParams(this);
        }
    }
    
}
