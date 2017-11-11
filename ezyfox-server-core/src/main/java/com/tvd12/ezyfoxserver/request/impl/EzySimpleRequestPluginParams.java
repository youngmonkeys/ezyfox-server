package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.request.EzyRequestPluginParams;

import lombok.Getter;

@Getter
public class EzySimpleRequestPluginParams implements EzyRequestPluginParams {

    protected EzyArray data;
    protected String pluginName;
    
    protected EzySimpleRequestPluginParams(Builder builder) {
        this.data = builder.data;
        this.pluginName = builder.pluginName;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyRequestPluginParams> {

        private EzyArray data;
        protected String pluginName;
        
        public Builder data(EzyArray data) {
            this.data = data;
            return this;
        }
        
        public Builder pluginName(String pluginName) {
            this.pluginName = pluginName;
            return this;
        }
        
        @Override
        public EzyRequestPluginParams build() {
            return new EzySimpleRequestPluginParams(this);
        }
    }
    
}
