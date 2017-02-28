package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.factory.impl.EzySessionFactoryImpl;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzySessionManagerImpl 
		extends EzyObjectPool<EzySession> 
		implements EzySessionManager {
	
	protected EzySessionFactory sessionFactory;
	protected ConcurrentHashMap<String, EzySession> sessionsByToken;
	
	{
		sessionsByToken = new ConcurrentHashMap<>();
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
	public EzySession borrowSession() {
		EzySession session = borrowObject();
		sessionsByToken.put(session.getToken(), session);
		return session;
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
