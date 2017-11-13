package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserAddableManager;

public class EzyAppUserManagerImpl
        extends EzySimpleUserAddableManager
        implements EzyAppUserManager {
    
    protected EzyAppUserManagerImpl(Builder builder) {
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyAppUserManager> {
        @Override
        public EzyAppUserManager build() {
            return new EzyAppUserManagerImpl(this);
        }
    }
    
}
