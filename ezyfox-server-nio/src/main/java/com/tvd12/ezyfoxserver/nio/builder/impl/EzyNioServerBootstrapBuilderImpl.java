package com.tvd12.ezyfoxserver.nio.builder.impl;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.api.EzyProxyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyProxyStreamingApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.nio.EzyNioServerBootstrap;
import com.tvd12.ezyfoxserver.nio.builder.EzyNioServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.nio.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.nio.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSocketStreamQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketStreamQueue;

public class EzyNioServerBootstrapBuilderImpl
		extends EzyHttpServerBootstrapBuilder 
		implements EzyNioServerBootstrapBuilder {

	@Override
	protected EzyServerBootstrap newServerBootstrap() {
		ExecutorService statsThreadPool = newStatsThreadPool();
		ExecutorService codecThreadPool = newCodecThreadPool();
		EzyCodecFactory codecFactory = newCodecFactory();
		EzyStreamingApi streamingApi = newStreamingApi();
		EzyResponseApi responseApi = newResponseApi(codecFactory);
		EzySocketRequestQueues requestQueues = newRequestQueues();
		EzySocketStreamQueue streamQueue = newStreamQueue();
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
				streamQueue,
				socketDisconnectionQueue,
				handlerGroupBuilderFactory);
		EzyNioServerBootstrap bootstrap = new EzyNioServerBootstrap();
		bootstrap.setResponseApi(responseApi);
		bootstrap.setStreamingApi(streamingApi);
		bootstrap.setRequestQueues(requestQueues);
		bootstrap.setStreamQueue(streamQueue);
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
			EzySocketStreamQueue streamQueue,
			EzySocketDisconnectionQueue disconnectionQueue,
			EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory) {
		
		return EzyHandlerGroupManagerImpl.builder()
				.serverContext(serverContext)
				.requestQueues(requestQueues)
				.streamQueue(streamQueue)
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
	
	protected EzyStreamingApi newStreamingApi() {
		return new EzyProxyStreamingApi();
	}
	
	protected EzyResponseApi newResponseApi(EzyCodecFactory codecFactory) {
		return new EzyProxyResponseApi(codecFactory);
	}
	
	private ExecutorService newStatsThreadPool() {
		int threadPoolSize = getThreadPoolSizeSetting().getStatistics();
		return EzyExecutors.newFixedThreadPool(threadPoolSize, "statistics");
	}
	
	private ExecutorService newCodecThreadPool() {
		int threadPoolSize = getThreadPoolSizeSetting().getCodec();
		return EzyExecutors.newFixedThreadPool(threadPoolSize, "codec");
	}
	
	private EzySocketRequestQueues newRequestQueues() {
		return new EzySimpleSocketRequestQueues();
	}
	
	private EzySocketStreamQueue newStreamQueue() {
		return new EzyBlockingSocketStreamQueue();
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
				.socketSetting(getSocketSetting())
				.websocketSetting(getWebsocketSetting())
				.build();
    }
	
}
