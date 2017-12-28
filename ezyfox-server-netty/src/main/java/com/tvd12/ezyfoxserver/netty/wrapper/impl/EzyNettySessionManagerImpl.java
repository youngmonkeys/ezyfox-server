package com.tvd12.ezyfoxserver.netty.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.netty.factory.EzyNettySessionFactory;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyNettySessionManager;
import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNettySessionManagerImpl 
		extends EzySimpleSessionManager<EzyNettySession> 
		implements EzyNettySessionManager {

	protected ConcurrentHashMap<Object, EzyNettySession> sessionsByConnection
			= new ConcurrentHashMap<>();
	
	protected EzyNettySessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void unmapSession(EzyNettySession session) {
		super.unmapSession(session);
		EzyChannel channel = session.getChannel();
		Object connection = channel.getConnection();
		sessionsByConnection.remove(connection);
	}
	
	@Override
	public EzyNettySession provideSession(EzyChannel channel) {
		EzyNettySession ss = provideSession(channel.getConnectionType());
		ss.setChannel(channel);
		sessionsByConnection.put(channel.getConnection(), ss);
		return ss;
	}
	
	@Override
	public EzyNettySession getSession(EzyChannel channel) {
		Object connection = channel.getConnection();
		return connection != null ? sessionsByConnection.get(connection) : null;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleSessionManager.Builder<EzyNettySession> {
		
		@Override
		public EzyNettySessionManagerImpl build() {
			return new EzyNettySessionManagerImpl(this);
		}

		@Override
		protected EzyObjectFactory<EzyNettySession> newObjectFactory() {
			return new EzyNettySessionFactory();
		}
	}
	
	
}
