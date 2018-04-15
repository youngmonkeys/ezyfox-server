package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

import lombok.Getter;

import com.tvd12.ezyfoxserver.wrapper.EzyAbstractByMaxUserManager;

public class EzyAppUserManagerImpl
        extends EzyAbstractByMaxUserManager
        implements EzyAppUserManager {
    
    @Getter
    protected final String appName;
    
    protected EzyAppUserManagerImpl(Builder builder) {
        super(builder);
        this.appName = builder.appName; 
    }
    
    @Override
    public EzyUser removeUser(EzyUser user) {
         super.removeUser(user);
         getLogger().info("app: {} remove user: {}, remain users = {}", appName, user, usersById.size());
         return user;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyAbstractByMaxUserManager.Builder<Builder> {
        
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
