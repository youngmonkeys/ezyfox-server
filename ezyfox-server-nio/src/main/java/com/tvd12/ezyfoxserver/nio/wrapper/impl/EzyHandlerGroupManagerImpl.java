package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.nio.delegate.EzySocketChannelDelegate;
import com.tvd12.ezyfoxserver.nio.factory.EzyCodecFactory;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyHandlerGroupManagerImpl
		extends EzyLoggable
		implements EzyHandlerGroupManager, EzySocketChannelDelegate, EzyDestroyable {

	private final ExecutorService statsThreadPool;
	private final ExecutorService codecThreadPool;
	private final ExecutorService handlerThreadPool;
	
	private final EzyCodecFactory codecFactory;
	private final EzyServerContext serverContext;
	
	private final Map<Object, EzyHandlerGroup> groupsByConnection;
	private final EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;
	
	public EzyHandlerGroupManagerImpl(Builder builder) {
		this.statsThreadPool = builder.statsThreadPool;
		this.codecThreadPool = builder.codecThreadPool;
		this.handlerThreadPool = builder.handlerThreadPool;
		this.codecFactory = builder.codecFactory;
		this.serverContext = builder.serverContext;
		this.handlerGroupBuilderFactory = builder.handlerGroupBuilderFactory;
		this.groupsByConnection = new ConcurrentHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type) {
		EzyHandlerGroup group = newHandlerGroupBuilder(type)
				.channel(channel)
				.channelDelegate(this)
				.serverContext(serverContext)
				.decoder(newDataDecoder(type))
				.encoder(newDataEncoder(type))
				.statsThreadPool(statsThreadPool)
				.codecThreadPool(codecThreadPool)
				.handlerThreadPool(handlerThreadPool)
				.build();
		groupsByConnection.put(channel.getConnection(), group);
		return (T) group;
	}
	
	private EzyAbstractHandlerGroup.Builder newHandlerGroupBuilder(EzyConnectionType type) {
		return handlerGroupBuilderFactory.newBuilder(type);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EzyHandlerGroup> T getHandlerGroup(Object connection) {
		return (T) groupsByConnection.get(connection);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EzyHandlerGroup> T removeHandlerGroup(Object connection) {
		EzyHandlerGroup group = groupsByConnection.get(connection);
		if(group != null)
			group.fireChannelInactive();
		return (T)group;
	}
	
	@Override
	public void onChannelInactivated(EzyChannel channel) {
		groupsByConnection.remove(channel.getConnection());
	}
	
	private Object newDataDecoder(EzyConnectionType type) {
		return codecFactory.newDecoder(type);
	}
	
	private Object newDataEncoder(EzyConnectionType type) {
		return codecFactory.newEncoder(type);
	}
	
	@Override
	public void destroy() {
		groupsByConnection.clear();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyHandlerGroupManager> {
		private ExecutorService statsThreadPool;
		private ExecutorService codecThreadPool;
		private ExecutorService handlerThreadPool;
		
		private EzyServerContext serverContext;
		private EzyCodecFactory codecFactory;
		
		private EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;
		
		public Builder statsThreadPool(ExecutorService statsThreadPool) {
			this.statsThreadPool = statsThreadPool;
			return this;
		}
		
		public Builder codecThreadPool(ExecutorService codecThreadPool) {
			this.codecThreadPool = codecThreadPool;
			return this;
		}
		
		public Builder handlerThreadPool(ExecutorService handlerThreadPool) {
			this.handlerThreadPool = handlerThreadPool;
			return this;
		}
		
		public Builder codecFactory(EzyCodecFactory codecFactory) {
			this.codecFactory = codecFactory;
			return this;
		}
		
		public Builder serverContext(EzyServerContext serverContext) {
			this.serverContext = serverContext;
			return this;
		}
		
		public Builder handlerGroupBuilderFactory(EzyHandlerGroupBuilderFactory factory) {
			this.handlerGroupBuilderFactory = factory;
			return this;
		}
		
		@Override
		public EzyHandlerGroupManager build() {
			return new EzyHandlerGroupManagerImpl(this);
		}
		
	}
	
}
