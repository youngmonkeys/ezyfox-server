package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.factory.impl.EzySessionFactoryImpl;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.Channel;

public class EzySessionManagerImpl 
		extends EzyObjectPool<EzySession> 
		implements EzySessionManager {
	
	protected EzySessionFactory sessionFactory;
	protected ConcurrentHashMap<String, EzySession> sessionsByToken;
	protected ConcurrentHashMap<Channel, EzySession> sessionsByChannel;
	
	{
		sessionsByToken = new ConcurrentHashMap<>();
		sessionsByChannel = new ConcurrentHashMap<>();
	}
	
	protected EzySessionManagerImpl(Builder builder) {
		super(builder);
		this.sessionFactory = builder.getSessionFactory();
	}
	
	@Override
	public void returnSession(EzySession session) {
		sessionsByToken.remove(session.getToken());
		returnObject(session);
	}
	
	@Override
	public EzySession borrowSession(Channel channel) {
		EzySession session = borrowObject();
		sessionsByChannel.putIfAbsent(channel, session);
		sessionsByToken.putIfAbsent(session.getToken(), session);
		return session;
	}
	
	@Override
	public EzySession getSession(Channel channel) {
		return sessionsByChannel.get(channel);
	}

	@Override
	protected EzySession createObject() {
		return newSession();
	}
	
	@Override
	protected void removeObject(EzySession object) {
		sessionsByToken.remove(object.getToken());
	}
	
	protected EzySession newSession() {
		return sessionFactory.newSession();
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
