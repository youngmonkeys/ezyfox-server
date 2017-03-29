package com.tvd12.ezyfoxserver.wrapper.impl;

import java.security.KeyPair;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.factory.impl.EzySessionFactoryImpl;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGeneration;
import com.tvd12.ezyfoxserver.service.impl.EzySessionTokenGenerationImpl;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.Channel;

public class EzySessionManagerImpl 
		extends EzyObjectPool<EzySession> 
		implements EzySessionManager {
	
	protected EzySessionFactory sessionFactory;
	private EzySessionTokenGeneration tokenGeneration;
	protected ConcurrentHashMap<String, EzySession> sessionsByToken;
	protected ConcurrentHashMap<Channel, EzySession> sessionsByChannel;
	
	{
		sessionsByToken = new ConcurrentHashMap<>();
		sessionsByChannel = new ConcurrentHashMap<>();
		tokenGeneration = new EzySessionTokenGenerationImpl();
	}
	
	protected EzySessionManagerImpl(Builder builder) {
		super(builder);
		this.sessionFactory = builder.getSessionFactory();
	}
	
	@Override
	public void returnSession(EzySession session) {
		sessionsByToken.remove(session.getReconnectToken());
		sessionsByChannel.remove(session.getChannel());
		returnObject(session);
	}
	
	@Override
	public EzySession borrowSession(Channel channel) {
		KeyPair keyPair = newKeyPair();
		EzySession session = borrowObject();
		session.setChannel(channel);
		session.setReconnectToken(newTokenSession());
		session.setPublicKey(keyPair.getPublic().getEncoded());
		session.setPrivateKey(keyPair.getPrivate().getEncoded());
		sessionsByChannel.putIfAbsent(channel, session);
		sessionsByToken.putIfAbsent(session.getReconnectToken(), session);
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
		EzySession session = newSession();
		session.setReconnectToken(newTokenSession());
		return session;
	}
	
	@Override
	protected void removeObject(EzySession object) {
		sessionsByToken.remove(object.getReconnectToken());
	}
	
	protected EzySession newSession() {
		return sessionFactory.newSession();
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

		protected EzySessionFactory sessionFactory;
		
		public Builder sessionFactory(EzySessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
			return this;
		}
		
		protected EzySessionFactory getSessionFactory() {
			return sessionFactory != null 
					? sessionFactory : newSessionFactory();
		}
		
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
		
	}

}
