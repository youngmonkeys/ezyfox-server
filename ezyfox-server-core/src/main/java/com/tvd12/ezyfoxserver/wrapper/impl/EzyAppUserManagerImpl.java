package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserAddableManager;

public class EzyAppUserManagerImpl
        extends EzySimpleUserAddableManager
        implements EzyAppUserManager {
    
    protected EzyAppUserManagerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzySimpleUserAddableManager.Builder<Builder> {
        @Override
        public EzyAppUserManager build() {
            return new EzyAppUserManagerImpl(this);
        }
    }
    
}
