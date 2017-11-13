package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;

import lombok.Getter;

@Getter
public class EzySimpleAccessAppParams implements EzyAccessAppParams {

    private String appName;
    
    protected EzySimpleAccessAppParams(Builder builder) {
    	this.appName = builder.appName;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyAccessAppParams> {
        
        private String appName;
        
        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }
        
        @Override
        public EzyAccessAppParams build() {
            return new EzySimpleAccessAppParams(this);
        }
    }
    
}
