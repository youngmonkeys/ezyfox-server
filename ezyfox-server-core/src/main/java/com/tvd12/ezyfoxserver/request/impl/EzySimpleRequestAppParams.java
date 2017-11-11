package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;

import lombok.Getter;

@Getter
public class EzySimpleRequestAppParams implements EzyRequestAppParams {

    protected int appId;
    protected EzyArray data;
    
    protected EzySimpleRequestAppParams(Builder builder) {
        this.data = builder.data;
        this.appId = builder.appId;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyRequestAppParams> {

        private int appId;
        private EzyArray data;
        
        public Builder appId(int appId) {
            this.appId = appId;
            return this;
        }
        
        public Builder data(EzyArray data) {
            this.data = data;
            return this;
        }
        
        @Override
        public EzyRequestAppParams build() {
            return new EzySimpleRequestAppParams(this);
        }
    }
    
}
