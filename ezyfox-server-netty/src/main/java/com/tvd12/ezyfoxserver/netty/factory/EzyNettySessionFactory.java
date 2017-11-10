package com.tvd12.ezyfoxserver.netty.factory;

import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.netty.entity.EzySimpleSession;

public class EzyNettySessionFactory extends EzyAbstractSessionFactory<EzyNettySession> {

	@Override
	protected EzyNettySession newSession() {
		return new EzySimpleSession();
	}
	
}
