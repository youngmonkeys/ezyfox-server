package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.nio.EzyNioServerBootstrap;
import com.tvd12.ezyfoxserver.nio.api.EzyNioResponseApi;
import com.tvd12.ezyfoxserver.nio.builder.EzyNioServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.constant.EzyNioThreadPoolSizes;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;

public class EzyNioServerBootstrapBuilderImpl
		extends EzyHttpServerBootstrapBuilder 
		implements EzyNioServerBootstrapBuilder {

	@Override
	protected EzyServerBootstrap newServerBootstrap() {
		ExecutorService statsThreadPool = newStatsThreadPool();
		ExecutorService codecThreadPool = newCodecThreadPool();
		EzyCodecFactory codecFactory = newCodecFactory();
		EzyResponseApi responseApi = newResponseApi(codecFactory);
		EzySocketRequestQueues requestQueues = newRequestQueues();
		EzySessionTicketsQueue socketSessionTicketsQueue = newSocketSessionTicketsQueue();
		EzySessionTicketsQueue websocketSessionTicketsQueue = newWebSocketSessionTicketsQueue();
		EzySocketDisconnectionQueue socketDisconnectionQueue = newSocketDisconnectionQueue();
		EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = newHandlerGroupBuilderFactory(
				socketSessionTicketsQueue,
				websocketSessionTicketsQueue);
		EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager(
				statsThreadPool, 
				codecThreadPool, 
				codecFactory,
				requestQueues,
				socketDisconnectionQueue,
				handlerGroupBuilderFactory);
		EzyNioServerBootstrap bootstrap = new EzyNioServerBootstrap();
		bootstrap.setResponseApi(responseApi);
		bootstrap.setRequestQueues(requestQueues);
		bootstrap.setHandlerGroupManager(handlerGroupManager);
		bootstrap.setSocketDisconnectionQueue(socketDisconnectionQueue);
		bootstrap.setSocketSessionTicketsQueue(socketSessionTicketsQueue);
		bootstrap.setWebsocketSessionTicketsQueue(websocketSessionTicketsQueue);
		bootstrap.setSslContext(newSslContext(getWebsocketSetting().getSslConfig()));
		return bootstrap;
	}
	
	private EzyHandlerGroupManager newHandlerGroupManager(
			ExecutorService statsThreadPool,
			ExecutorService codecThreadPool,
			EzyCodecFactory codecFactory,
			EzySocketRequestQueues requestQueues,
			EzySocketDisconnectionQueue disconnectionQueue,
			EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory) {
		
		return EzyHandlerGroupManagerImpl.builder()
				.serverContext(serverContext)
				.requestQueues(requestQueues)
				.codecFactory(codecFactory)
				.statsThreadPool(statsThreadPool)
				.codecThreadPool(codecThreadPool)
				.disconnectionQueue(disconnectionQueue)
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
	
	protected EzyResponseApi newResponseApi(EzyCodecFactory codecFactory) {
		return EzyNioResponseApi.builder()
				.codecFactory(codecFactory)
				.build();
	}

	private ExecutorService newStatsThreadPool() {
		return EzyExecutors.newFixedThreadPool(EzyNioThreadPoolSizes.STATISTICS, "statistics");
	}
	
	private ExecutorService newCodecThreadPool() {
		return EzyExecutors.newFixedThreadPool(EzyNioThreadPoolSizes.CODEC, "codec");
	}
	
	private EzySocketRequestQueues newRequestQueues() {
		return new EzySimpleSocketRequestQueues();
	}
	
	private EzySessionTicketsQueue newSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	private EzySessionTicketsQueue newWebSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	private EzySocketDisconnectionQueue newSocketDisconnectionQueue() {
	    return new EzyBlockingSocketDisconnectionQueue();
	}

	private EzyCodecFactory newCodecFactory() {
		return EzySimpleCodecFactory.builder()
				.socketSettings(getSocketSetting())
				.websocketSettings(getWebsocketSetting())
				.build();
    }
	
}
