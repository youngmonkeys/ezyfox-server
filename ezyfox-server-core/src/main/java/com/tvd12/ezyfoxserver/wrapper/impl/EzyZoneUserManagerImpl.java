package com.tvd12.ezyfoxserver.wrapper.impl;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyProcessor;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.delegate.EzyUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractByMaxUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public class EzyZoneUserManagerImpl 
        extends EzyAbstractByMaxUserManager 
        implements EzyZoneUserManager, EzyStartable, EzyDestroyable {

    protected final EzyUserDelegate userDelegate;
    protected final ScheduledExecutorService idleValidationService;
    protected final ConcurrentHashMap<EzySession, EzyUser> usersBySession = new ConcurrentHashMap<>();
    
    protected EzyZoneUserManagerImpl(Builder builder) {
        super(builder);
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
		getLogger().info("add user {}, user count = {}", user.getName(), usersById.size());
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
	public void unmapSessionUser(EzySession session) {
	    EzyUser user = usersBySession.remove(session);
	    if(user != null) {
	        user.removeSession(session);
	        getLogger().debug("remove session {} from user {}, remain {} sessions", session.getClientAddress(), user.getName(), user.getSessionCount());
	        if(user.getSessionCount() == 0 && user.getMaxIdleTime() <= 0) {
	            removeUser(user, EzyUserRemoveReason.DISCONNECT);
	        }
	    }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#removeUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser)
	 */
	@Override
	public EzyUser removeUser(EzyUser user) {
		if(user != null) {
		    super.removeUser(user);
			removeUserSessions(user);
		}
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyServerUserManager#removeUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser, com.tvd12.ezyfoxserver.mapping.constant.EzyUserRemoveReason)
	 */
	@Override
	public void removeUser(EzyUser user, EzyUserRemoveReason reason) {
	    EzyProcessor.processWithLock(
	            () -> tryRemoveUser(user, reason), getLock(user.getName()));
	    getLogger().info("server: remove user: {}, reason: {}, remain users = {}", user, reason, usersById.size());
	}
	
	public void tryRemoveUser(EzyUser user, EzyUserRemoveReason reason) {
	    removeUser(user);
        delegateUserRemove(user, reason);
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
	    getLogger().debug("start user manager");
	    startIdleValidationService();
	}
	
	protected void startIdleValidationService() {
	    if(idleValidationService != null) {
	        idleValidationService.scheduleAtFixedRate(
	                this::validateIdleUsers, 3000, 100, TimeUnit.MILLISECONDS);
	    }
	}
	
	protected void validateIdleUsers() {
	    for(EzyUser user : getUserList())
	        if(isIdleUser(user))
	            removeUser(user, EzyUserRemoveReason.IDLE);
	}
	
	protected boolean isIdleUser(EzyUser user) {
	    return user.isIdle();
	}
	
	protected void removeUserSessions(EzyUser user) {
	    for(EzySession session : user.getSessions())
	        usersBySession.remove(session);
	}
	
	protected void delegateUserRemove(EzyUser user, EzyUserRemoveReason reason) {
	    userDelegate.onUserRemoved(user, reason);
    }
	
	@Override
	public void destroy() {
	    if(idleValidationService != null)
	        processWithLogException(idleValidationService::shutdown);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractByMaxUserManager.Builder<Builder> {
		
	    protected long maxIdleTime;
	    protected EzyUserDelegate userDelegate;
	    
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
