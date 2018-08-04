package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyNioSessionFactory;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNioSessionManagerImpl 
		extends EzySimpleSessionManager<EzyNioSession> 
		implements EzyNioSessionManager {

	protected ConcurrentHashMap<Object, EzyNioSession> sessionsByConnection
			= new ConcurrentHashMap<>();
	
	protected EzyNioSessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void unmapSession(EzyNioSession session) {
		super.unmapSession(session);
		sessionsByConnection.remove(session.getChannel());
	}
	
	@Override
	protected void clearSession(EzyNioSession session) {
		super.clearSession(session);
	}
	
	@Override
	public EzyNioSession getSession(Object connection) {
		return sessionsByConnection.get(connection);
	}
	
	@Override
	public EzyNioSession provideSession(EzyChannel channel) {
		EzyNioSession ss = provideSession(channel.getConnectionType());
		ss.setChannel(channel);
		sessionsByConnection.put(channel.getConnection(), ss);
		return ss;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleSessionManager.Builder<EzyNioSession> {
		
		@Override
		public EzyNioSessionManagerImpl build() {
			return new EzyNioSessionManagerImpl(this);
		}

		@Override
		protected EzyObjectFactory<EzyNioSession> newObjectFactory() {
			return new EzyNioSessionFactory();
		}
	}
	
	
}
