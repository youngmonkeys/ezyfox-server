package com.tvd12.ezyfoxserver.netty.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.netty.factory.EzyNettySessionFactory;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettyChannel;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyNettySessionManager;
import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

import io.netty.channel.Channel;

public class EzyNettySessionManagerImpl 
		extends EzySimpleSessionManager<EzyNettySession> 
		implements EzyNettySessionManager {

	protected ConcurrentHashMap<Channel, EzyNettySession> sessionsByChannel
			= new ConcurrentHashMap<>();
	
	protected EzyNettySessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void unmapSession(EzyNettySession session) {
		super.unmapSession(session);
		sessionsByChannel.remove(session.getChannel());
	}
	
	@Override
	protected void clearSession(EzyNettySession session) {
		super.clearSession(session);
		session.setChannel(null);
	}
	
	@Override
	public EzyNettySession getSession(Channel channel) {
		return sessionsByChannel.get(channel);
	}
	
	@Override
	public EzyNettySession borrowSession(Channel channel, EzyConnectionType type) {
		EzyNettySession ss = borrowSession(type);
		ss.setChannel(new EzyNettyChannel(channel, type));
		sessionsByChannel.put(channel, ss);
		return ss;
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
