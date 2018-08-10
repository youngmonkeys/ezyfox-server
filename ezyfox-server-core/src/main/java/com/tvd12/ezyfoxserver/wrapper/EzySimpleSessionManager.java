package com.tvd12.ezyfoxserver.wrapper;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCoreConstants;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyLockName;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyDisconnectReasonAware;
import com.tvd12.ezyfoxserver.entity.EzyHasSessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyMaxSessionException;
import com.tvd12.ezyfox.pattern.EzyObjectProvider;
import com.tvd12.ezyfox.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfox.util.EzyEntry;
import com.tvd12.ezyfox.util.EzyProcessor;
import com.tvd12.ezyfox.util.EzyTimes;

public class EzySimpleSessionManager<S extends EzySession> 
		extends EzyObjectProvider<S> 
		implements EzySessionManager<S> {
	
    protected final int maxSessions;
	protected final EzySessionTokenGenerator tokenGenerator;
	protected final ConcurrentHashMap<String, S> loggedInSession = new ConcurrentHashMap<>();
	protected final ConcurrentHashMap<Long, S> sessionsById = new ConcurrentHashMap<>();
	protected final ConcurrentHashMap<String, S> sessionsByToken = new ConcurrentHashMap<>();
	protected final ConcurrentLinkedQueue<Entry<S, EzyConstant>> disconnectedSessions = new ConcurrentLinkedQueue<>();
	
	protected static final AtomicInteger COUNTER = new AtomicInteger(0);
	
	protected EzySimpleSessionManager(Builder<S> builder) {
		super(builder);
		this.maxSessions = builder.maxSessions;
		this.tokenGenerator = builder.getSessionTokenGenerator();
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
	public void addDisconnectedSession(S session, EzyConstant reason) {
	    synchronized (disconnectedSessions) {
	        disconnectedSessions.add(EzyEntry.of(session, reason));
        }
	}
	
	@Override
	public void removeSession(S session) {
	    removeSession(session, EzyDisconnectReason.UNKNOWN);
	}
	
	@Override
	public void removeSession(S session, EzyConstant reason) {
	    checkToRemoveSession(session, reason);
	}
	
    protected void doRemoveSession(S session, EzyConstant reason) {
        unmapSession(session);
        notifySessionRemoved(session, reason);
        clearSession(session);
        getLogger().info("remove session, remain sessions = {}", providedObjects.size());
    }
	
	protected void checkToRemoveSession(S session, EzyConstant reason) {
	    if(shouldRemoveSession(session)) 
	        removeSessionWithLock(session, reason);
	}
	
	protected void removeSessionWithLock(S session, EzyConstant reason) {
	    ((EzyDisconnectReasonAware)session).setDisconnectReason(reason);
	    Lock lock = session.getLock(EzyLockName.REMOVE);
	    EzyProcessor.processWithTryLock(() -> doRemoveSession(session, reason), lock);
	}
	
	protected boolean shouldRemoveSession(S session) {
	    return session != null && containsSession(session.getReconnectToken());
	}
	
	protected void unmapSession(S session) {
	    providedObjects.remove(session);
	    sessionsById.remove(session.getId());
		sessionsByToken.remove(session.getReconnectToken());
		loggedInSession.remove(session.getReconnectToken());
	}
	
	protected void clearSession(S session) {
	    getLogger().info("session {} inactive", session.getClientAddress());
	    session.destroy();
	}
	
	protected void notifySessionRemoved(S session, EzyConstant reason) {
		if(session != null)
		    notifySessionRemoved0(session, reason);
	}
	
	protected void notifySessionRemoved0(S session, EzyConstant reason) {
		try {
		    EzyHasSessionDelegate hasDelegate = (EzyHasSessionDelegate)session;
		    EzySessionDelegate delegate = hasDelegate.getDelegate();
		    delegate.onSessionRemoved(reason);
		}
		catch(Exception e) {
			getLogger().debug("notify session removed error", e);
		}
	}
	
	@SuppressWarnings("unchecked")
    protected S provideSession(EzyConstant connectionType) {
	    checkMaxSessions(connectionType);
		KeyPair keyPair = newKeyPair();
		EzyAbstractSession session = (EzyAbstractSession)provideObject();
		session.setLoggedIn(false);
		session.setName("Session#" + COUNTER.incrementAndGet());
		session.setConnectionType(connectionType);
		session.setReconnectToken(newSessionToken());
		session.setCreationTime(System.currentTimeMillis());
		session.setPublicKey(keyPair.getPublic().getEncoded());
		session.setPrivateKey(keyPair.getPrivate().getEncoded());
		
		session.setCreationTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastWriteTime(System.currentTimeMillis());
        S complete = (S)session;
        sessionsById.put(complete.getId(), complete);
		sessionsByToken.put(complete.getReconnectToken(), complete);
		getLogger().debug("provide session, sessions size = {}", providedObjects.size());
		return complete;
	}
	
	protected void checkMaxSessions(EzyConstant connectionType) {
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
	public List<S> getAllSessions() {
	    return getProvidedObjects();
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
	    return providedObjects.size();
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
	
	protected Queue<Entry<S, EzyConstant>> getDisconnectedSessions() {
	    Queue<Entry<S, EzyConstant>> queue = new LinkedList<>();
	    synchronized (disconnectedSessions) {
            while (!disconnectedSessions.isEmpty()) {
                queue.add(disconnectedSessions.poll());
            }
        }
	    return queue;
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		getLogger().debug("start session manager");
	}
	
	@Override
	protected void removeStaleObjects() {
	    checkAndRemoveSessions();
	    removeDisconnectedSesions();
	}
	
	protected void checkAndRemoveSessions() {
	    List<S> idleSessions = new ArrayList<>();
        List<S> unloggedInSessions = new ArrayList<>();
        for(S session : getAllSessions()) {
            if(isUnloggedInSession(session)) {
                unloggedInSessions.add(session);
            }
            else if(isIdleSession(session)) {
                idleSessions.add(session);
            }
        }
        for(S session : idleSessions) {
            removeSession(session, EzyDisconnectReason.IDLE);
        }
        
        for(S session : unloggedInSessions) {
            removeSession(session, EzyDisconnectReason.NOT_LOGGED_IN);
        }
	}
	
	protected void removeDisconnectedSesions() {
	    Queue<Entry<S, EzyConstant>> sessions = getDisconnectedSessions();
        while(!sessions.isEmpty()) {
            Entry<S, EzyConstant> entry = sessions.poll();
            removeSession(entry.getKey(), entry.getValue());
        }
	}
	
	protected boolean isIdleSession(S session) {
	    return session.isIdle();
	}
	
	protected boolean isUnloggedInSession(EzySession session) {
	    if(session.isLoggedIn())
	        return false;
	    if(!session.isActivated())
	        return false;
		return getSessionRemainWaitingTime(session) <= 0; 
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
				.keysize(EzyCoreConstants.SESSION_KEY_SIZE)
				.algorithm(EzyCoreConstants.DATA_ENCRYPTION_ALGORITHM)
				.build().generate();
	}
	
	@Override
	public void destroy() {
	    super.destroy();
	    this.loggedInSession.clear();
	    this.sessionsById.clear();
	    this.sessionsByToken.clear();
	    this.disconnectedSessions.clear();
	}
	
	public abstract static class Builder<S extends EzySession> 
			extends EzyObjectProvider.Builder<S, Builder<S>> {

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
