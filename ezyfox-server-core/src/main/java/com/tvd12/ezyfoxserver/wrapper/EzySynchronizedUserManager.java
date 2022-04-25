package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class EzySynchronizedUserManager extends EzyAbstractUserManager {

    @Getter
    protected final Object synchronizedLock = new Object();

    public EzySynchronizedUserManager(int maxUser) {
        super(maxUser);
    }

    protected EzySynchronizedUserManager(Builder<?> builder) {
        super(builder);
    }

    public static Builder<?> builder() {
        return new Builder<>();
    }

    @Override
    public EzyUser addUser(EzyUser user) {
        EzyUser answer;
        synchronized (synchronizedLock) {
            answer = addUser0(user);
        }
        logger.info("{} add user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", getMessagePrefix(), user, locks.size(), usersById.size(), usersByName.size());
        return answer;
    }

    @Override
    public EzyUser getUser(long userId) {
        synchronized (synchronizedLock) {
            return super.getUser(userId);
        }
    }

    @Override
    public EzyUser getUser(String username) {
        synchronized (synchronizedLock) {
            return super.getUser(username);
        }
    }

    @Override
    public List<EzyUser> getUserList() {
        synchronized (synchronizedLock) {
            return super.getUserList();
        }
    }

    @Override
    public boolean containsUser(long userId) {
        synchronized (synchronizedLock) {
            return super.containsUser(userId);
        }
    }

    @Override
    public boolean containsUser(String username) {
        synchronized (synchronizedLock) {
            return super.containsUser(username);
        }
    }

    @Override
    public EzyUser removeUser(EzyUser user) {
        synchronized (synchronizedLock) {
            removeUser0(user);
        }
        logger.info("{} remove user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", getMessagePrefix(), user, locks.size(), usersById.size(), usersByName.size());
        return user;
    }

    @Override
    public int getUserCount() {
        synchronized (synchronizedLock) {
            return super.getUserCount();
        }
    }

    @Override
    public boolean available() {
        synchronized (synchronizedLock) {
            return super.available();
        }
    }

    @Override
    public Lock getLock(String username) {
        synchronized (synchronizedLock) {
            return super.getLock(username);
        }
    }

    @Override
    public void removeLock(String username) {
        synchronized (synchronizedLock) {
            super.removeLock(username);
        }
    }

    @Override
    public void clear() {
        synchronized (synchronizedLock) {
            super.clear();
        }
    }

    @Override
    public void destroy() {
        clear();
    }

    public static class Builder<B extends Builder<B>> extends EzyAbstractUserManager.Builder<B> {

        @Override
        public EzySynchronizedUserManager build() {
            return new EzySynchronizedUserManager(this);
        }

    }
}
