package com.tvd12.ezyfoxserver.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfox.util.EzyLoggable;

public abstract class EzyAbstractUserManager extends EzyLoggable implements EzyUserManager {

    protected final int maxUsers;
    protected final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<Long, EzyUser> usersById = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, EzyUser> usersByName = new ConcurrentHashMap<>();
    
    protected EzyAbstractUserManager(Builder<?> builder) {
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
        return locks.computeIfAbsent(username, k -> new ReentrantLock());
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
    
    public abstract static class Builder<B extends Builder<B>> 
            implements EzyBuilder<EzyUserManager> {
        
        protected int maxUsers = 999999;
        
        @SuppressWarnings("unchecked")
        public B maxUsers(int maxUsers) {
            this.maxUsers = maxUsers;
            return (B)this;
        }
    }
}
