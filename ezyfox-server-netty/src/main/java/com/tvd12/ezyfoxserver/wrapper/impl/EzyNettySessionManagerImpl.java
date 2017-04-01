package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.factory.EzyNettySessionFactory;
import com.tvd12.ezyfoxserver.factory.EzySessionFactory;
import com.tvd12.ezyfoxserver.wrapper.EzyNettySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

import io.netty.channel.Channel;

public class EzyNettySessionManagerImpl 
		extends EzySimpleSessionManager<EzyNettySession> 
		implements EzyNettySessionManager {

	protected ConcurrentHashMap<Channel, EzyNettySession> sessionsByChannel;
	
	protected EzyNettySessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void initComponents(AbstractBuilder builder) {
		super.initComponents(builder);
		sessionsByChannel = new ConcurrentHashMap<>();
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
	public EzyNettySession borrowSession(Channel channel) {
		EzyNettySession ss = borrowSession();
		ss.setChannel(channel);
		sessionsByChannel.putIfAbsent(channel, ss);
		return ss;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleSessionManager.Builder<EzyNettySession> {
		@Override
		protected EzySessionFactory<EzyNettySession> newSessionFactory() {
			return new EzyNettySessionFactory();
		}
		
		@Override
		protected EzyNettySessionManagerImpl newProduct() {
			return new EzyNettySessionManagerImpl(this);
		}
		
		@Override
		public EzyNettySessionManagerImpl build() {
			return (EzyNettySessionManagerImpl) super.build();
		}
	}
	
	
}
