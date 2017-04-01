package com.tvd12.ezyfoxserver.factory;

import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;

public abstract class EzyAbstractSessionFactory<S extends EzySession> 
		implements EzySessionFactory<S> {

	private final AtomicInteger counter = new AtomicInteger(0);

	@Override
	public S newProduct() {
		S session = newSession();
		initSession((EzyAbstractSession)session);
		return session;
	}
	
	protected void initSession(EzyAbstractSession session) {
		session.setId(counter.incrementAndGet());
		session.setCreationTime(System.currentTimeMillis());
	}
	
	protected abstract S newSession();
	
}
