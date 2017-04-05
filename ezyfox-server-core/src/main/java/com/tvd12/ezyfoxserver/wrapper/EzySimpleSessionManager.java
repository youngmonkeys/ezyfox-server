package com.tvd12.ezyfoxserver.wrapper;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGeneration;
import com.tvd12.ezyfoxserver.service.impl.EzySessionTokenGenerationImpl;
import com.tvd12.ezyfoxserver.util.EzyTimes;

public class EzySimpleSessionManager<S extends EzySession> 
		extends EzyObjectPool<S> 
		implements EzySessionManager<S> {
	
	protected EzySessionTokenGeneration tokenGeneration;
	protected ConcurrentHashMap<String, S> sessionsByToken;
	
	protected EzySimpleSessionManager(Builder<S> builder) {
		super(builder);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initComponents(AbstractBuilder builder) {
		super.initComponents(builder);
		sessionsByToken = new ConcurrentHashMap<>();
		tokenGeneration = new EzySessionTokenGenerationImpl();
	}
	
	@Override
	public boolean containsSession(String token) {
		return sessionsByToken.contains(token);
	}
	
	@Override
	public void returnSession(S session) {
		returnSession(session, null);
	}
	
	@Override
	public void returnSession(S session, EzyConstant reason) {
		unmapSession(session);
		returnObject(session);
		notifySessionReturned(session, reason);
		clearSession(session);
		getLogger().info("return session {}, remain = {}", session, borrowedObjects.size());
	}
	
	protected void unmapSession(S session) {
		sessionsByToken.remove(session.getReconnectToken());
	}
	
	protected void clearSession(S session) {
		session.setDelegate(null);
		session.setActivated(false);
		session.setLoggedIn(false);
	}
	
	protected void notifySessionReturned(S session, EzyConstant reason) {
		if(reason != null)
			tryNotifySessionReturned(session, reason);
	}
	
	protected void tryNotifySessionReturned(S session, EzyConstant reason) {
		try {
			session.getDelegate().onSessionReturned(reason);
		}
		catch(Exception e) {
			getLogger().debug("notify session returned error", e);
		}
	}
	
	protected S borrowSession() {
		KeyPair keyPair = newKeyPair();
		S session = borrowObject();
		session.setActivated(true);
		session.setReconnectToken(newTokenSession());
		session.setCreationTime(System.currentTimeMillis());
		session.setPublicKey(keyPair.getPublic().getEncoded());
		session.setPrivateKey(keyPair.getPrivate().getEncoded());
		sessionsByToken.putIfAbsent(session.getReconnectToken(), session);
		getLogger().info("borrow session {}, sessions size = {}", session, borrowedObjects.size());
		return session;
	}
	
	@Override
	public S getSession(String token) {
		return sessionsByToken.get(token);
	}

	@Override
	protected void removeStaleObjects() {
		removeUnloggedInSessions();
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		getLogger().debug("start session manager");
	}
	
	protected void removeUnloggedInSessions() {
		removeUnloggedInSessions(new ArrayList<>(borrowedObjects));
	}
	
	protected void removeUnloggedInSessions(Collection<S> sessions) {
		sessions.forEach(this::removeUnloggedInSession);
	}
	
	protected void removeUnloggedInSession(S session) {
		if(isUnloggedInSession(session))
			returnSession(session, EzySessionRemoveReason.NOT_LOGGED_IN);
			
	}
	
	protected boolean isUnloggedInSession(EzySession session) {
		if(session.isLoggedIn())
			return false;
		return getSessionRemainWaitingTime(session) <= 0;
	}
	
	protected long getSessionRemainWaitingTime(EzySession session) {
		return EzyTimes.getRemainTime(
				session.getMaxWaitingTime(), session.getCreationTime());
	}
	
	protected String newTokenSession() {
		return tokenGeneration.generate();
	}
	
	protected KeyPair newKeyPair() {
		return EzyKeysGenerator.builder()
				.keyLength(512)
				.algorithm("RSA")
				.build().generate();
	}

	public abstract static class Builder<S extends EzySession> 
			extends AbstractBuilder<S, Builder<S>> {

		protected abstract EzySessionFactory<S> newSessionFactory();
		
		@Override
		protected String getProductName() {
			return EzySessionManager.class.getSimpleName();
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected EzySimpleSessionManager newProduct() {
			return new EzySimpleSessionManager(this);
		}
		
		@Override
		protected void prepare() {
			super.prepare();
			this.objectFactory = newSessionFactory();
		}
		
		@Override
		public EzySimpleSessionManager<S> build() {
			return (EzySimpleSessionManager<S>) super.build();
		}
		
	}

}
