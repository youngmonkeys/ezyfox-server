package com.tvd12.ezyfoxserver.factory;

import com.tvd12.ezyfoxserver.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleSession;

public class EzyNettySessionFactory extends EzyAbstractSessionFactory<EzyNettySession> {

	@Override
	protected EzyNettySession newSession() {
		return new EzySimpleSession();
	}
	
}
