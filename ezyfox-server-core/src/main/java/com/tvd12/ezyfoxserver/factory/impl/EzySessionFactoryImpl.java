package com.tvd12.ezyfoxserver.factory.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleSession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGeneration;
import com.tvd12.ezyfoxserver.service.impl.EzySessionTokenGenerationImpl;

public class EzySessionFactoryImpl implements EzySessionFactory {

	private EzySessionTokenGeneration tokenGeneration;
	private static final AtomicInteger SESSION_COUNTER;
	
	{
		tokenGeneration = new EzySessionTokenGenerationImpl();
	}
	
	static {
		SESSION_COUNTER = new AtomicInteger(0);
	}
	
	@Override
	public EzySession newSession() {
		EzySimpleSession session = new EzySimpleSession();
		session.setId(SESSION_COUNTER.incrementAndGet());
		session.setCreationTime(System.currentTimeMillis());
		session.setToken(tokenGeneration.generate());
		return newSession();
	}
	
}
