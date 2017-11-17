package com.tvd12.ezyfoxserver.nio.factory;

import com.tvd12.ezyfoxserver.factory.EzyAbstractSessionFactory;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.entity.EzySimpleSession;
import com.tvd12.ezyfoxserver.socket.EzyNonBlockingPacketQueue;

public class EzyNioSessionFactory extends EzyAbstractSessionFactory<EzyNioSession> {

	@Override
	protected EzyNioSession newSession() {
		EzySimpleSession session = new EzySimpleSession();
		session.setPacketQueue(new EzyNonBlockingPacketQueue());
		return session;
	}
	
}
