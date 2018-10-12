package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.creator.EzySessionCreator;
import com.tvd12.ezyfoxserver.creator.EzySimpleSessionCreator;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroup;

public class EzyHandlerGroupManagerImpl
		extends EzyLoggable
		implements EzyHandlerGroupManager {

	private final ExecutorService statsThreadPool;
	private final ExecutorService codecThreadPool;
	
	private final EzyCodecFactory codecFactory;
	private final EzyServerContext serverContext;
	private final EzySessionCreator sessionCreator;
	private final EzySocketRequestQueues requestQueues;
	private final EzySocketDisconnectionQueue disconnectionQueue;
	
	private final Map<Object, EzyHandlerGroup> groupsByConnection;
	private final EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;
	
	public EzyHandlerGroupManagerImpl(Builder builder) {
		this.statsThreadPool = builder.statsThreadPool;
		this.codecThreadPool = builder.codecThreadPool;
		this.requestQueues = builder.requestQueues;
		this.disconnectionQueue = builder.disconnectionQueue;
		this.codecFactory = builder.codecFactory;
		this.serverContext = builder.serverContext;
		this.sessionCreator = builder.sessionCreator;
		this.handlerGroupBuilderFactory = builder.handlerGroupBuilderFactory;
		this.groupsByConnection = new ConcurrentHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type) {
		EzyNioSession session = sessionCreator.create(channel);
		EzyHandlerGroup group = newHandlerGroupBuilder(type)
				.session(session)
				.serverContext(serverContext)
				.requestQueues(requestQueues)
				.disconnectionQueue(disconnectionQueue)
				.decoder(newDataDecoder(type))
				.statsThreadPool(statsThreadPool)
				.codecThreadPool(codecThreadPool)
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
	
	@Override
	public EzySocketDataHandlerGroup removeHandlerGroup(EzySession session) {
		if(session == null)
			return null;
		if(session.getChannel() == null)
			return null;
		if(session.getChannel().getConnection() == null)
			return null;
		EzyChannel channel = session.getChannel();
		Object connection = channel.getConnection();
		EzyHandlerGroup group = groupsByConnection.remove(connection);
		getLogger().debug("remove handler group: {} with session: {}", group, session);
		return group;
	}
	
	private EzyHandlerGroup getHandlerGroup(EzySession session) {
		if(session == null)
			return null;
		EzyChannel channel = session.getChannel();
		if(channel == null)
			return null;
		Object connection = channel.getConnection();
		if(connection == null)
			return null;
		return groupsByConnection.get(connection);
	}
	
	@Override
	public EzySocketDataHandlerGroup getDataHandlerGroup(EzySession session) {
		return getHandlerGroup(session);
	}
	
	@Override
	public EzySocketWriterGroup getWriterGroup(EzySession session) {
		return getHandlerGroup(session);
	}
	
	private Object newDataDecoder(EzyConnectionType type) {
		return codecFactory.newDecoder(type);
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

		private EzyCodecFactory codecFactory;
		private EzyServerContext serverContext;
		private EzySessionCreator sessionCreator;
		private EzySocketRequestQueues requestQueues;
		private EzySocketDisconnectionQueue disconnectionQueue;
		private EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory;
		
		public Builder statsThreadPool(ExecutorService statsThreadPool) {
			this.statsThreadPool = statsThreadPool;
			return this;
		}
		
		public Builder codecThreadPool(ExecutorService codecThreadPool) {
			this.codecThreadPool = codecThreadPool;
			return this;
		}
		
		public Builder requestQueues(EzySocketRequestQueues requestQueues) {
			this.requestQueues = requestQueues;
			return this;
		}
		
		public Builder disconnectionQueue(EzySocketDisconnectionQueue disconnectionQueue) {
			this.disconnectionQueue = disconnectionQueue;
			return this;
		}
		
		public Builder codecFactory(EzyCodecFactory codecFactory) {
			this.codecFactory = codecFactory;
			return this;
		}
		
		public Builder serverContext(EzyServerContext serverContext) {
			this.serverContext = serverContext;
			this.sessionCreator = newSessionCreator(serverContext);
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
		
		protected EzySessionCreator newSessionCreator(EzyServerContext serverContext) {
			EzyServer server = serverContext.getServer();
			EzySettings settings = server.getSettings();
			return EzySimpleSessionCreator.builder()
					.sessionManager(server.getSessionManager())
					.sessionSetting(settings.getSessionManagement())
					.build();
		}
		
	}
	
}
