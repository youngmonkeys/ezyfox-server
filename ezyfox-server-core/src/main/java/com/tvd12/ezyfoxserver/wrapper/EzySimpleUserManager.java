package com.tvd12.ezyfoxserver.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleUserManager extends EzyLoggable implements EzyUserManager {

    protected final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<Long, EzyUser> usersById = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, EzyUser> usersByName = new ConcurrentHashMap<>();
    
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
            getLogger().info("remove user {}, remain users = {}", user.getName(), usersById.size());
        }
        return user;
    }
    
    @Override
    public Lock getLock(String username) {
        return locks.computeIfAbsent(username, k -> new ReentrantLock());
    }
    
    @Override
    public void removeLock(String username) {
        locks.remove(username);
    }
}
