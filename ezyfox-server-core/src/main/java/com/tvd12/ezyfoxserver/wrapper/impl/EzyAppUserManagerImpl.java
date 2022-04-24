package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.function.EzyFunctions;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.delegate.EzyAppUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import lombok.Getter;

import java.util.concurrent.locks.Lock;

public class EzyAppUserManagerImpl
    extends EzyAbstractUserManager implements EzyAppUserManager {

    @Getter
    protected final String appName;
    protected final EzyAppUserDelegate userDelegate;

    protected EzyAppUserManagerImpl(Builder builder) {
        super(builder);
        this.appName = builder.appName;
        this.userDelegate = builder.userDelegate;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void removeUser(EzyUser user, EzyConstant reason) {
        String username = user.getName();
        Lock lock = locks.computeIfAbsent(username, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
        lock.lock();
        try {
            boolean contains = containsUser(user);
            if (contains) {
                removeUser(user);
                userDelegate.onUserRemoved(user, reason);
            }
        } finally {
            lock.unlock();
            locks.remove(username);
        }
    }

    @Override
    protected String getMessagePrefix() {
        return "app: " + appName;
    }

    @Override
    public void destroy() {
        super.destroy();
        ((EzyDestroyable) userDelegate).destroy();
    }

    public static class Builder extends EzyAbstractUserManager.Builder<Builder> {

        protected String appName;
        protected EzyAppUserDelegate userDelegate;

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder userDelegate(EzyAppUserDelegate userDelegate) {
            this.userDelegate = userDelegate;
            return this;
        }

        @Override
        public EzyAppUserManager build() {
            return new EzyAppUserManagerImpl(this);
        }
    }

}
