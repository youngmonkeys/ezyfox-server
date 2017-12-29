package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserAddableManager;

public class EzyAppUserManagerImpl
        extends EzySimpleUserAddableManager
        implements EzyAppUserManager {
    
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
    
    public static class Builder extends EzySimpleUserAddableManager.Builder<Builder> {
        
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
