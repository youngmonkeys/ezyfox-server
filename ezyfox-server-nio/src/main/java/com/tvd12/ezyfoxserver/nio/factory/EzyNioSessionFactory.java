package com.tvd12.ezyfoxserver.nio.factory;

import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;

public class EzyNioSessionFactory extends EzyAbstractSessionFactory<EzyNioSession> {

	@Override
	protected EzyNioSession newSession() {
		EzySimpleSession session = new EzySimpleSession();
		return session;
	}
	
}
