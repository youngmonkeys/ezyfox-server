package com.tvd12.ezyfoxserver.wrapper.impl;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.factory.impl.EzySessionFactoryImpl;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGeneration;
import com.tvd12.ezyfoxserver.service.impl.EzySessionTokenGenerationImpl;
import com.tvd12.ezyfoxserver.util.EzyTimes;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.Channel;

public class EzySessionManagerImpl 
		extends EzyObjectPool<EzySession> 
		implements EzySessionManager {
	
	private EzySessionTokenGeneration tokenGeneration;
	protected ConcurrentHashMap<String, EzySession> sessionsByToken;
	protected ConcurrentHashMap<Channel, EzySession> sessionsByChannel;
	
	protected EzySessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initComponents(AbstractBuilder builder) {
		super.initComponents(builder);
		sessionsByToken = new ConcurrentHashMap<>();
		sessionsByChannel = new ConcurrentHashMap<>();
		tokenGeneration = new EzySessionTokenGenerationImpl();
	}
	
	@Override
	public void returnSession(EzySession session) {
		returnSession(session, null);
	}
	
	@Override
	public void returnSession(EzySession session, EzyConstant reason) {
		sessionsByToken.remove(session.getReconnectToken());
		sessionsByChannel.remove(session.getChannel());
		returnObject(session);
		notifySessionReturned(session, reason);
		session.setChannel(null);
		session.setDelegate(null);
		session.setActivated(false);
		getLogger().info("return session {}, remain = {}", session, borrowedObjects.size());
	}
	
	protected void notifySessionReturned(EzySession session, EzyConstant reason) {
		if(reason != null)
			session.getDelegate().onSessionReturned(reason);
	}
	
	@Override
	public EzySession borrowSession(Channel channel) {
		KeyPair keyPair = newKeyPair();
		EzySession session = borrowObject();
		session.setChannel(channel);
		session.setActivated(true);
		session.setReconnectToken(newTokenSession());
		session.setCreationTime(System.currentTimeMillis());
		session.setPublicKey(keyPair.getPublic().getEncoded());
		session.setPrivateKey(keyPair.getPrivate().getEncoded());
		sessionsByChannel.putIfAbsent(channel, session);
		sessionsByToken.putIfAbsent(session.getReconnectToken(), session);
		getLogger().info("borrow session {}, sessions size = {}", session, borrowedObjects.size());
		return session;
	}
	
	@Override
	public EzySession getSession(Channel channel) {
		return sessionsByChannel.get(channel);
	}
	
	@Override
	public EzySession getSession(String token) {
		return sessionsByToken.get(token);
	}

	@Override
	protected EzySession createObject() {
		EzySession session = super.createObject();
		session.setReconnectToken(newTokenSession());
		return session;
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
	
	protected void removeUnloggedInSessions(Collection<EzySession> sessions) {
		sessions.forEach(this::removeUnloggedInSession);
	}
	
	protected void removeUnloggedInSession(EzySession session) {
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

	public static class Builder extends AbstractBuilder<EzySession, Builder> {

		protected EzySessionFactory newSessionFactory() {
			return new EzySessionFactoryImpl();
		}
		
		@Override
		protected String getProductName() {
			return EzySessionManager.class.getSimpleName();
		}

		@Override
		protected EzySessionManagerImpl newProduct() {
			return new EzySessionManagerImpl(this);
		}
		
		@Override
		protected void prepare() {
			super.prepare();
			this.objectFactory = newSessionFactory();
		}
		
	}

}
