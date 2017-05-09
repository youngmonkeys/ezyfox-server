package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;
import com.tvd12.ezyfoxserver.entity.EzyHasUserRemoveDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.util.EzyStartable;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleUserManager;

public class EzyServerUserManagerImpl 
        extends EzySimpleUserManager 
        implements EzyServerUserManager, EzyStartable {

    protected final ScheduledExecutorService idleValidationService;
    protected final ConcurrentHashMap<EzySession, EzyUser> usersBySession = new ConcurrentHashMap<>();
    
    public EzyServerUserManagerImpl() {
        this.idleValidationService = EzyExecutors.newScheduledThreadPool(3, "user-manager");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> idleValidationService.shutdown()));
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
	    usersBySession.remove(session);
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
	    idleValidationService.scheduleAtFixedRate(
	            this::validateIdleUsers, 3000, 1000, TimeUnit.MILLISECONDS);
	}
	
	protected void validateIdleUsers() {
	    for(EzyUser user : getUserList())
	        if(isIdleUser(user))
	            removeUser(user, EzyUserRemoveReason.IDLE);
	}
	
	protected boolean isIdleUser(EzyUser user) {
	    return user.getSessionCount() == 0 &&
	            user.getMaxIdleTime() < System.currentTimeMillis() - user.getStartIdleTime();
	}
	
	protected void removeUserSessions(EzyUser user) {
	    for(EzySession session : user.getSessions())
	        usersBySession.remove(session);
	}
	
	protected void delegateUserRemove(EzyUser user, EzyUserRemoveReason reason) {
	    EzyHasUserRemoveDelegate hasDelegate = (EzyHasUserRemoveDelegate)user;
	    EzyUserRemoveDelegate delegate = hasDelegate.getRemoveDelegate();
	    delegate.onUserRemoved(reason);
    }
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyServerUserManager> {
		
		@Override
		public EzyServerUserManager build() {
			return new EzyServerUserManagerImpl();
		}
		
	}
	
}
