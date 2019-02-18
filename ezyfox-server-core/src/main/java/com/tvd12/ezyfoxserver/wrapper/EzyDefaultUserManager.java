package com.tvd12.ezyfoxserver.wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyDefaultUserManager extends EzyAbstractUserManager {

    public EzyDefaultUserManager(int maxUser) {
        super(maxUser);
    }
    
    protected EzyDefaultUserManager(Builder<?> builder) {
        super(builder);
    }
    
    @Override
    protected Map<String, Lock> newLocksMap() {
        return new HashMap<>();
    }
    
    @Override
    protected Map<Long, EzyUser> newUsersByIdMap() {
        return new HashMap<>();
    }
    
    @Override
    protected Map<String, EzyUser> newUsersByName() {
        return new HashMap<>();
    }
    
    public static Builder<?> builder() {
        return new Builder<>();
    }

    public static class Builder<B extends Builder<B>> extends EzyAbstractUserManager.Builder<B> {
        
        @Override
        public EzyUserManager build() {
            return new EzyDefaultUserManager(this);
        }
    }
}
