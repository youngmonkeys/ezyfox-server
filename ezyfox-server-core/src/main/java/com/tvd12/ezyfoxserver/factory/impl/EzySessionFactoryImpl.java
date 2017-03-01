package com.tvd12.ezyfoxserver.factory.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.impl.EzySimpleSession;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;

public class EzySessionFactoryImpl implements EzySessionFactory {

	private final AtomicInteger counter;

	{
		counter = new AtomicInteger(0);
	}
	
	@Override
	public EzySession newSession() {
		EzySimpleSession session = new EzySimpleSession();
		session.setId(counter.incrementAndGet());
		session.setCreationTime(System.currentTimeMillis());
		return session;
	}
	
}
