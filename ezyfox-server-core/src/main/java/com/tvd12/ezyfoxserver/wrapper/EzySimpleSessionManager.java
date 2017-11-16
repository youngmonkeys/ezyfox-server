package com.tvd12.ezyfoxserver.wrapper;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyLockName;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyHasSessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyMaxSessionException;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.util.EzyIfElse;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.util.EzyTimes;

public class EzySimpleSessionManager<S extends EzySession> 
		extends EzyObjectPool<S> 
		implements EzySessionManager<S> {
	
    protected final int maxSessions;
	protected final EzySessionTokenGenerator tokenGenerator;
	protected final ScheduledExecutorService idleValidationService;
	protected final ConcurrentHashMap<String, S> loggedInSession = new ConcurrentHashMap<>();
	protected final ConcurrentHashMap<Long, S> sessionsById = new ConcurrentHashMap<>();
	protected final ConcurrentHashMap<String, S> sessionsByToken = new ConcurrentHashMap<>();
	
	protected static final AtomicInteger COUNTER = new AtomicInteger(0);
	
	protected EzySimpleSessionManager(Builder<S> builder) {
		super(builder);
		this.initializeObjects();
		this.maxSessions = builder.maxSessions;
		this.tokenGenerator = builder.getSessionTokenGenerator();
		this.idleValidationService = EzyExecutors.newScheduledThreadPool(3, "session-manager");
	}
	
	@Override
	public void addLoggedInSession(S session) {
	    loggedInSession.put(session.getReconnectToken(), session);
	}
	
	@Override
	public boolean containsSession(long id) {
	    return sessionsById.containsKey(id);
	}
	
	@Override
	public boolean containsSession(String token) {
		return sessionsByToken.containsKey(token);
	}
	
	@Override
	public void returnSession(S session) {
		returnSession(session, null);
	}
	
	@Override
	public void returnSession(S session, EzyConstant reason) {
	    checkToReturnSession(session, reason);
	}
	
    protected void doReturnSession(S session, EzyConstant reason) {
        unmapSession(session);
        notifySessionReturned(session, reason);
        clearSession(session);
        returnObject(session);
        getLogger().debug("return session, remain sessions = {}", borrowedObjects.size());
    }
	
	protected void checkToReturnSession(S session, EzyConstant reason) {
	    if(shouldReturnSession(session)) 
	        returnSessionWithLock(session, reason);
	}
	
	protected void returnSessionWithLock(S session, EzyConstant reason) {
	    Lock lock = session.getLock(EzyLockName.REMOVE);
	    EzyProcessor.processWithTryLock(() -> doReturnSession(session, reason), lock);
	}
	
	protected boolean shouldReturnSession(S session) {
	    return session != null && containsSession(session.getReconnectToken());
	}
	
	protected void unmapSession(S session) {
	    sessionsById.remove(session.getId());
		sessionsByToken.remove(session.getReconnectToken());
		loggedInSession.remove(session.getReconnectToken());
	}
	
	protected void clearSession(S session) {
	    getLogger().info("session {} inactive", session.getClientAddress());
	    session.destroy();
	}
	
	protected void notifySessionReturned(S session, EzyConstant reason) {
		EzyIfElse.withIf(reason != null, () -> tryNotifySessionReturned(session, reason));
	}
	
	protected void tryNotifySessionReturned(S session, EzyConstant reason) {
		try {
		    EzyHasSessionDelegate hasDelegate = (EzyHasSessionDelegate)session;
		    hasDelegate.getDelegate().onSessionReturned(reason);
		}
		catch(Exception e) {
			getLogger().debug("notify session returned error", e);
		}
	}
	
	@SuppressWarnings("unchecked")
    protected S borrowSession(EzyConnectionType type) {
	    checkMaxSessions(type);
		KeyPair keyPair = newKeyPair();
		EzyAbstractSession session = (EzyAbstractSession)borrowObject();
		session.setLoggedIn(false);
		session.setName("Session#" + COUNTER.incrementAndGet());
		session.setConnectionType(type);
		session.setReconnectToken(newSessionToken());
		session.setCreationTime(System.currentTimeMillis());
		session.setPublicKey(keyPair.getPublic().getEncoded());
		session.setPrivateKey(keyPair.getPrivate().getEncoded());
		
		session.setCreationTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastWriteTime(System.currentTimeMillis());
        session.setActivated(true);
        S complete = (S)session;
        sessionsById.put(complete.getId(), complete);
		sessionsByToken.put(complete.getReconnectToken(), complete);
		getLogger().debug("borrow session, sessions size = {}", borrowedObjects.size());
		return complete;
	}
	
	protected void checkMaxSessions(EzyConnectionType type) {
	    int current = getAliveSessionCountWithLock();
	    if(current >= maxSessions)
	        throw new EzyMaxSessionException(current, maxSessions);
	}
	
	@Override
	public EzySession getSession(long id) {
	    return sessionsById.get(id);
	}
	
	@Override
	public S getSession(String token) {
		return sessionsByToken.get(token);
	}
	
	@Override
	public Set<S> getAllSessions() {
	    Set<S> all = new HashSet<>();
	    all.addAll(getRemainObjects());
	    all.addAll(getBorrowedObjects());
	    return all;
	}
	
	@Override
	public List<S> getAliveSessions() {
	    return new ArrayList<>(sessionsByToken.values());
	};
	
	@Override
	public List<S> getLoggedInSessions() {
	    return new ArrayList<>(loggedInSession.values());
	}
	
	@Override
	public int getAllSessionCount() {
	    return pool.size() + borrowedObjects.size();
	}

	@Override
	public int getAliveSessionCount() {
	    return sessionsByToken.size();
	}
	
	@Override
	public int getLoggedInSessionCount() {
	    return loggedInSession.size();
	}
	
	public int getAliveSessionCountWithLock() {
        return returnWithLock(this::getAllSessionCount);
    }
	
	@Override
	public void start() throws Exception {
		super.start();
		startIdleValidationService();
		getLogger().debug("start session manager");
	}
	
	protected void startIdleValidationService() {
	    idleValidationService.scheduleAtFixedRate(
	            this::validateIdleSessions, 3000, 1000, TimeUnit.MILLISECONDS);
	}
	
	protected void validateIdleSessions() {
	    for(S session : getLoggedInSessions())
	        if(isIdleSession(session))
	            returnSession(session, EzySessionRemoveReason.IDLE);
	}
	
	protected boolean isIdleSession(S session) {
	    return session.getMaxIdleTime() 
	            < (System.currentTimeMillis() - session.getLastReadTime());
	}

	@Override
	protected boolean isStaleObject(S session) {
	    return session.isActivated() && isUnloggedInSession(session);
	}
	
	@Override
	protected void removeStaleObject(S session) {
	    returnSession(session, EzySessionRemoveReason.NOT_LOGGED_IN);
	}
	
	@Override
	protected List<S> getCanBeStaleObjects() {
	    return EzyLists.newArrayList(getBorrowedObjects(), getLoggedInSessions());
	}
	
	protected boolean isUnloggedInSession(EzySession session) {
		return session.isLoggedIn() ? false : getSessionRemainWaitingTime(session) <= 0; 
	}
	
	protected long getSessionRemainWaitingTime(EzySession session) {
		return EzyTimes.getRemainTime(
				session.getMaxWaitingTime(), session.getCreationTime());
	}
	
	protected String newSessionToken() {
		return tokenGenerator.generate();
	}
	
	protected KeyPair newKeyPair() {
		return EzyKeysGenerator.builder()
				.keysize(512)
				.algorithm("RSA")
				.build().generate();
	}
	
	@Override
	protected void tryShutdown() {
	    super.tryShutdown();
	    this.idleValidationService.shutdown();
	}

	public abstract static class Builder<S extends EzySession> 
			extends EzyObjectPool.Builder<S, Builder<S>> {

	    protected int maxSessions = 999999;
	    protected EzySessionTokenGenerator tokenGenerator;
	    
		@Override
		protected String getProductName() {
			return "session-manager";
		}
		
		public Builder<S> maxSessions(int maxSessions) {
		    this.maxSessions = maxSessions;
		    return this;
		}

		public Builder<S> tokenGenerator(EzySessionTokenGenerator generator) {
		    this.tokenGenerator = generator;
		    return this;
		}
		
		protected EzySessionTokenGenerator getSessionTokenGenerator() {
		    return tokenGenerator != null ? tokenGenerator : new EzySimpleSessionTokenGenerator();
		}
		
		public abstract EzySimpleSessionManager<S> build();
		
	}

}
