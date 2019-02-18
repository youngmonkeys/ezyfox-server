package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.wrapper.EzyAbstractUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

import lombok.Getter;

public class EzyAppUserManagerImpl 
        extends EzyAbstractUserManager implements EzyAppUserManager {
    
    @Getter
    protected final String appName;
    
    protected EzyAppUserManagerImpl(Builder builder) {
        super(builder);
        this.appName = builder.appName; 
    }
    
    @Override
    protected String getMessagePrefix() {
        return "app: " + appName;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyAbstractUserManager.Builder<Builder> {
        
        protected String appName;
        
        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }
        
        @Override
        public EzyAppUserManager build() {
            return new EzyAppUserManagerImpl(this);
        }
    }
    
}
