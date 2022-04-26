package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.function.EzyFunctions;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public abstract class EzyAbstractUserManager extends EzyLoggable implements EzyUserManager {

    protected final int maxUsers;
    protected final Map<String, Lock> locks = newLocksMap();
    protected final Map<Long, EzyUser> usersById = newUsersByIdMap();
    protected final Map<String, EzyUser> usersByName = newUsersByName();

    public EzyAbstractUserManager(int maxUser) {
        this.maxUsers = maxUser;
    }

    protected EzyAbstractUserManager(Builder<?> builder) {
        this.maxUsers = builder.maxUsers;
    }

    @Override
    public EzyUser addUser(EzyUser user) {
        EzyUser answer = doAddUser(user);
        logger.info(
            "{} add user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}",
            getMessagePrefix(),
            user,
            locks.size(),
            usersById.size(),
            usersByName.size()
        );
        return answer;
    }

    protected void checkMaxUsers() {
        int current = usersById.size();
        if (current >= maxUsers) {
            throw new EzyMaxUserException(current, maxUsers);
        }
    }

    protected EzyUser doAddUser(EzyUser user) {
        checkMaxUsers();
        usersByName.putIfAbsent(user.getName(), user);
        return usersById.putIfAbsent(user.getId(), user);
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
        doRemoveUser(user);
        logger.info(
            "{} remove user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}",
            getMessagePrefix(),
            user,
            locks.size(),
            usersById.size(),
            usersByName.size()
        );
        return user;
    }

    protected void doRemoveUser(EzyUser user) {
        if (user != null) {
            locks.remove(user.getName());
            usersById.remove(user.getId());
            usersByName.remove(user.getName());
        }
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
        return usersById.size() < maxUsers;
    }

    @Override
    public Lock getLock(String username) {
        return locks.computeIfAbsent(username, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
    }

    @Override
    public void removeLock(String username) {
        locks.remove(username);
    }

    @Override
    public void clear() {
        this.locks.clear();
        this.usersById.clear();
        this.usersByName.clear();
    }

    protected String getMessagePrefix() {
        return "user manager:";
    }

    @Override
    public void destroy() {
        clear();
    }

    protected Map<String, Lock> newLocksMap() {
        return new ConcurrentHashMap<>();
    }

    protected Map<Long, EzyUser> newUsersByIdMap() {
        return new ConcurrentHashMap<>();
    }

    protected Map<String, EzyUser> newUsersByName() {
        return new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<B>>
        implements EzyBuilder<EzyUserManager> {

        protected int maxUsers = 999999;

        public B maxUsers(int maxUsers) {
            this.maxUsers = maxUsers;
            return (B) this;
        }
    }
}
