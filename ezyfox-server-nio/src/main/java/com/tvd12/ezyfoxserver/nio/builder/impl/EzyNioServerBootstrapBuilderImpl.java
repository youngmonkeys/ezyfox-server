package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.nio.EzyNioServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.EzyNioServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.factory.EzyCodecFactory;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;

public class EzyNioServerBootstrapBuilderImpl
		extends EzyHttpServerBootstrapBuilder 
		implements EzyNioServerBootstrapBuilder {

	@Override
	protected EzyServerBootstrap newServerBootstrap() {
		ExecutorService statsThreadPool = newStatsThreadPool();
		ExecutorService codecThreadPool = newCodecThreadPool();
		ExecutorService handlerThreadPool = newHandlerThreadPool();
		EzySessionTicketsQueue socketSessionTicketsQueue = newSocketSessionTicketsQueue();
		EzySessionTicketsQueue websocketSessionTicketsQueue = newWebSocketSessionTicketsQueue();
		EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = newHandlerGroupBuilderFactory(
				socketSessionTicketsQueue,
				websocketSessionTicketsQueue);
		EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager(
				statsThreadPool, 
				codecThreadPool, 
				handlerThreadPool,
				handlerGroupBuilderFactory);
		EzyNioServerBootstrap bootstrap = new EzyNioServerBootstrap();
		bootstrap.setHandlerGroupManager(handlerGroupManager);
		bootstrap.setSocketSessionTicketsQueue(socketSessionTicketsQueue);
		bootstrap.setWebsocketSessionTicketsQueue(websocketSessionTicketsQueue);
		bootstrap.setSslContext(newSslContext(getWebsocketSettings().getSslConfig()));
		return bootstrap;
	}
	
	private EzyHandlerGroupManager newHandlerGroupManager(
			ExecutorService statsThreadPool,
			ExecutorService codecThreadPool,
			ExecutorService handlerThreadPool,
			EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory) {
		
		return EzyHandlerGroupManagerImpl.builder()
				.serverContext(serverContext)
				.codecFactory(newCodecFactory())
				.statsThreadPool(statsThreadPool)
				.codecThreadPool(codecThreadPool)
				.handlerThreadPool(handlerThreadPool)
				.handlerGroupBuilderFactory(handlerGroupBuilderFactory)
				.build();
	}
	
	private EzyHandlerGroupBuilderFactory newHandlerGroupBuilderFactory(
			EzySessionTicketsQueue socketSessionTicketsQueue,
			EzySessionTicketsQueue websocketSessionTicketsQueue) {
		
		return EzyHandlerGroupBuilderFactoryImpl.builder()
		        .statistics(server.getStatistics())
				.socketSessionTicketsQueue(socketSessionTicketsQueue)
				.webSocketSessionTicketsQueue(websocketSessionTicketsQueue)
				.build();
	}

	
	private ExecutorService newStatsThreadPool() {
		return EzyExecutors.newFixedThreadPool(3, "statistics");
	}
	
	private ExecutorService newCodecThreadPool() {
		return EzyExecutors.newFixedThreadPool(3, "codec");
	}
	
	private ExecutorService newHandlerThreadPool() {
		return EzyExecutors.newFixedThreadPool(3, "handler");
	}
	
	private EzySessionTicketsQueue newSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	private EzySessionTicketsQueue newWebSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}

	private EzyCodecFactory newCodecFactory() {
		return EzyCodecFactoryImpl.builder()
				.socketSettings(getSocketSettings())
				.websocketSettings(getWebsocketSettings())
				.build();
    }
	
}
