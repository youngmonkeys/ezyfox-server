package com.tvd12.ezyfoxserver.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyDefaultUserManager extends EzyLoggable implements EzyUserManager {

    protected final int maxUsers;
    protected final Map<String, Lock> locks = new HashMap<>();
    protected final Map<Long, EzyUser> usersById = new HashMap<>();
    protected final Map<String, EzyUser> usersByName = new HashMap<>();
    
    public EzyDefaultUserManager(int maxUser) {
        this.maxUsers = maxUser;
    }
    
    protected EzyDefaultUserManager(Builder<?> builder) {
        this.maxUsers = builder.maxUsers;
    }
    
    @Override
    public EzyUser getUser(long userId) {
        return usersById.get(userId);
    }

    @Override
    public EzyUser getUser(String username) {
        return usersByName.get(username);
    }
    
    @Override
    public List<EzyUser> getUserList() {
        return new ArrayList<>(usersById.values());
    }

    @Override
    public boolean containsUser(long userId) {
        return usersById.containsKey(userId);
    }

    @Override
    public boolean containsUser(String username) {
        return usersByName.containsKey(username);
    }

    @Override
    public EzyUser removeUser(EzyUser user) {
        if(user != null) {
            locks.remove(user.getName());
            usersById.remove(user.getId());
            usersByName.remove(user.getName());
        }
        logger.info("{} remove user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", getMessagePrefix(), user, locks.size(), usersById.size(), usersByName.size());
        return user;
    }
    
    @Override
    public int getUserCount() {
        return usersById.size();
    }
    
    @Override
    public int getMaxUsers() {
        return maxUsers;
    }
    
    @Override
    public boolean available() {
        return getUserCount() < maxUsers;
    }
    
    @Override
    public Lock getLock(String username) {
        return locks.computeIfAbsent(username, NEW_REENTRANTLOCK_FUNC);
    }
    
    @Override
    public void removeLock(String username) {
        locks.remove(username);
    }
    
    @Override
    public void clear() {
        this.unlockAll();
        this.locks.clear();
        this.usersById.clear();
        this.usersByName.clear();
    }
    
    protected void unlockAll() {
        for(Lock lock : locks.values())
            lock.unlock();
    }
    
    protected String getMessagePrefix() {
        return "user manager:";
    }
    
    @Override
    public void destroy() {
        clear();
    }
    
    public static Builder<?> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends Builder<B>> implements EzyBuilder<EzyUserManager> {
        
        protected int maxUsers = 999999;
        
        public B maxUsers(int maxUsers) {
            this.maxUsers = maxUsers;
            return (B)this;
        }
        
        @Override
        public EzyUserManager build() {
            return new EzyDefaultUserManager(this);
        }
    }
}
