package com.tvd12.ezyfoxserver.wrapper.impl;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.delegate.EzyUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractByMaxUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public class EzyZoneUserManagerImpl 
        extends EzyAbstractByMaxUserManager 
        implements EzyZoneUserManager, EzyStartable {

    protected final String zoneName;
    protected final EzyUserDelegate userDelegate;
    protected final ScheduledExecutorService idleValidationService;
    protected final ConcurrentHashMap<EzySession, EzyUser> usersBySession = new ConcurrentHashMap<>();
    
    protected EzyZoneUserManagerImpl(Builder builder) {
        super(builder);
        this.zoneName = builder.zoneName;
        this.userDelegate = builder.userDelegate;
        this.idleValidationService = newIdleValidationService(builder.maxIdleTime);
    }
    
    protected ScheduledExecutorService newIdleValidationService(long maxIdleTime) {
        if(maxIdleTime <= 0) return null;
        ScheduledExecutorService answer = EzyExecutors.newScheduledThreadPool(3, "user-manager");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> answer.shutdown()));
        return answer;
    }
    
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#addUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser)
	 */
	@Override
	public void addUser(EzySession session, EzyUser user) {
		usersById.put(user.getId(), user);
		usersByName.put(user.getName(), user);
		usersBySession.put(session, user);
		getLogger().info("zone: {} add user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", zoneName, user, locks.size(), usersById.size(), usersByName.size());
	}
	
	@Override
	public void bind(EzySession session, EzyUser user) {
	    usersBySession.put(session, user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#getUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
	 */
	@Override
	public EzyUser getUser(EzySession session) {
		return usersBySession.get(session);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#containsUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
	 */
	@Override
	public boolean containsUser(EzySession session) {
		return usersBySession.containsKey(session);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyServerUserManager#unmapSessionUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
	 */
	@Override
	public void unmapSessionUser(EzySession session, EzyConstant reason) {
	    EzyUser user = usersBySession.remove(session);
	    if(user != null) {
	        user.removeSession(session);
	        getLogger().debug("zone: {} remove session {} from user {} by reason {}, user remain {} sessions and {} usersBySession", zoneName, session.getClientAddress(), user, reason, user.getSessionCount(), usersBySession.size());
	        if(shouldRemoveUserNow(user)) 
	            removeUser(user, reason);
	    }
	}
	
	protected boolean shouldRemoveUserNow(EzyUser user) {
	    int sessionCount = user.getSessionCount();
	    long maxIdleTime = user.getMaxIdleTime();
	    boolean should = sessionCount <= 0 && maxIdleTime <= 0;
	    return should;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyServerUserManager#removeUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser, com.tvd12.ezyfoxserver.mapping.constant.EzyUserRemoveReason)
	 */
	@Override
	public void removeUser(EzyUser user, EzyConstant reason) {
	    Lock lock = getLock(user.getName());
	    lock.lock();
        try {
        	    removeUser0(user, reason);
        }
        finally {
            lock.unlock();
        }
	}
	
	private void removeUser0(EzyUser user, EzyConstant reason) {
	    getLogger().debug("zone: {} remove user: {} by reason: {}", zoneName, user, reason);
	    removeUser(user);
        delegateUserRemove(user, reason);
        destroyUser(user);
	}
	
	@Override
	public EzyUser findAndUpdateUser(EzySession current, EzySession update) {
	    EzyUser user = usersBySession.remove(current);
	    if(user == null) return null;
	    user.addSession(update);
	    user.removeSession(current);
	    return usersBySession.computeIfAbsent(update, k -> user);
	}
	
	@Override
	public void start() throws Exception {
	    getLogger().debug("start user manager for zone: {}", zoneName);
	    startIdleValidationService();
	}
	
	protected void startIdleValidationService() {
	    if(idleValidationService != null) {
	        idleValidationService.scheduleAtFixedRate(
	                this::validateIdleUsers, 3000, 100, TimeUnit.MILLISECONDS);
	    }
	}
	
	protected void validateIdleUsers() {
	    for(EzyUser user : getUserList()) {
	        if(isIdleUser(user))
	            removeUser(user, EzyDisconnectReason.IDLE);
	    }
	}
	
	protected boolean isIdleUser(EzyUser user) {
	    boolean idle = user.isIdle();
	    return idle;
	}
	
	protected void delegateUserRemove(EzyUser user, EzyConstant reason) {
	    userDelegate.onUserRemoved(user, reason);
    }
	
	protected void destroyUser(EzyUser user) {
	    user.destroy();
	}
	
	@Override
	public void destroy() {
	    super.destroy();
	    this.usersBySession.clear();
	    if(idleValidationService != null)
	        processWithLogException(idleValidationService::shutdown);
	}
	
	@Override
	protected String getMessagePrefix() {
	    return "zone: " + zoneName;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractByMaxUserManager.Builder<Builder> {

	    protected String zoneName;
	    protected long maxIdleTime;
	    protected EzyUserDelegate userDelegate;
	    
	    public Builder zoneName(String zoneName) {
	        this.zoneName = zoneName;
	        return this;
	    }
	    
	    public Builder maxIdleTime(long maxIdletime) {
	        this.maxIdleTime = maxIdletime;
	        return this;
	    }
	    
	    public Builder userDelegate(EzyUserDelegate userDelegate) {
	        this.userDelegate = userDelegate;
	        return this;
	    }
	    
		@Override
		public EzyZoneUserManager build() {
			return new EzyZoneUserManagerImpl(this);
		}
		
	}
	
}
