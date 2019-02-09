package com.tvd12.ezyfoxserver.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.Getter;

public class SynchronizedUserManager 
		extends EzyLoggable
		implements EzyUserManager {

	@Getter
	protected final int maxUsers;
	@Getter
    protected final Object synchronizedLock = new Object();
    protected final Map<String, Lock> locks = new HashMap<>();
    protected final Map<Long, EzyUser> usersById = new HashMap<>();
    protected final Map<String, EzyUser> usersByName = new HashMap<>();
    
    public SynchronizedUserManager(int maxUser) {
        this.maxUsers = maxUser;
    }
    
    protected SynchronizedUserManager(Builder<?> builder) {
        this.maxUsers = builder.maxUsers;
    }
    
    @Override
    public EzyUser getUser(long userId) {
    		synchronized(synchronizedLock) {
    			EzyUser user = usersById.get(userId);
    			return user;
    		}
    }

    @Override
    public EzyUser getUser(String username) {
    		synchronized(synchronizedLock) {
    			EzyUser user = usersByName.get(username);
    			return user;
    		}
    }
    
    @Override
    public List<EzyUser> getUserList() {
    		synchronized (synchronizedLock) {
    			List<EzyUser> users = new ArrayList<>(usersById.values());
    	        return users;	
		}
    }

    @Override
    public boolean containsUser(long userId) {
    		synchronized (synchronizedLock) {
	        boolean answer = usersById.containsKey(userId);
	        return answer;
    		}
    }

    @Override
    public boolean containsUser(String username) {
    		synchronized (synchronizedLock) {
    			boolean answer = usersByName.containsKey(username);
    			return answer;
    		}
    }

    @Override
    public EzyUser removeUser(EzyUser user) {
        if(user != null) {
	        	synchronized (synchronizedLock) {
	            locks.remove(user.getName());
	            usersById.remove(user.getId());
	            usersByName.remove(user.getName());
	        	}
        }
        logger.info("{} remove user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", getMessagePrefix(), user, locks.size(), usersById.size(), usersByName.size());
        return user;
    }
    
    @Override
    public int getUserCount() {
	    	synchronized (synchronizedLock) {
	        int count = usersById.size();
	        return count;
	    	}
    }
    
    @Override
    public boolean available() {
	    	synchronized (synchronizedLock) {
	    		int userCount = getUserCount();
	        boolean answer = userCount < maxUsers;
	        return answer;
	    	}
    }
    
    @Override
    public Lock getLock(String username) {
	    	synchronized (synchronizedLock) {
	        Lock lock = locks.computeIfAbsent(username, NEW_REENTRANTLOCK_FUNC);
	        return lock;
	    	}
    }
    
    @Override
    public void removeLock(String username) {
	    	synchronized (synchronizedLock) {
	        locks.remove(username);
	    	}
    }
    
    @Override
    public void clear() {
	    	synchronized (synchronizedLock) {
	        this.unlockAll();
	        this.locks.clear();
	        this.usersById.clear();
	        this.usersByName.clear();
	    	}
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
