package com.tvd12.ezyfoxserver.netty.builder.impl;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.netty.EzyNettyServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.builder.EzyNettyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.netty.constant.EzyNettyThreadPoolSizes;
import com.tvd12.ezyfoxserver.netty.factory.EzyHandlerGroupBuilderFactory;
import com.tvd12.ezyfoxserver.netty.wrapper.EzyHandlerGroupManager;
import com.tvd12.ezyfoxserver.netty.wrapper.impl.EzyHandlerGroupManagerImpl;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketRequestQueues;
import com.tvd12.ezyfoxserver.socket.EzySocketRequestQueues;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EzyNettyServerBootstrapBuilderImpl 
		extends EzyHttpServerBootstrapBuilder 
		implements EzyNettyServerBootstrapBuilder {

	protected EventLoopGroup childGroup;
	protected EventLoopGroup parentGroup;
	
	@Override
	protected void prebuild() {
		childGroup = newChildEventLoopGroup();
		parentGroup = newParentEventLoopGroup();
	}
	
	@Override
	protected EzyNettyServerBootstrap newServerBootstrap() {
		ExecutorService statsThreadPool = newStatsThreadPool();
		EzySocketRequestQueues requestQueues = newRequestQueues();
		EzySessionTicketsQueue socketSessionTicketsQueue = newSocketSessionTicketsQueue();
		EzySessionTicketsQueue websocketSessionTicketsQueue = newWebSocketSessionTicketsQueue();
		EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory = newHandlerGroupBuilderFactory(
				socketSessionTicketsQueue,
				websocketSessionTicketsQueue);
		EzyHandlerGroupManager handlerGroupManager = newHandlerGroupManager(
				statsThreadPool, 
				requestQueues,
				handlerGroupBuilderFactory);
		EzyNettyServerBootstrap answer = new EzyNettyServerBootstrap();
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
		answer.setRequestQueues(requestQueues);
		answer.setHandlerGroupManager(handlerGroupManager);
		answer.setSocketServerBootstrap(createSocketServerBootstrap(
				socketSessionTicketsQueue,
				handlerGroupManager));
		answer.setWebsocketServerBootstrap(createWebSocketServerBootstrap(
				websocketSessionTicketsQueue,
				handlerGroupManager));
		return answer;
	}
	
	protected EzySocketServerBootstrap createSocketServerBootstrap(
			EzySessionTicketsQueue sessionTicketsQueue,
			EzyHandlerGroupManager handlerGroupManager) {
		return createSocketServerBootstrap(EzySocketServerBootstrap.builder()
				.sessionTicketsQueue(sessionTicketsQueue)
				.handlerGroupManager(handlerGroupManager));
	}
	
	protected EzySocketServerBootstrap createWebSocketServerBootstrap(
			EzySessionTicketsQueue sessionTicketsQueue,
			EzyHandlerGroupManager handlerGroupManager) {
		return createSocketServerBootstrap(EzyWebSocketServerBootstrap.builder()
				.sslContextSupplier(this::newSslContext)
				.sessionTicketsQueue(sessionTicketsQueue)
				.handlerGroupManager(handlerGroupManager));
	}
	
	protected EzySocketServerBootstrap createSocketServerBootstrap(
			EzySocketServerBootstrap.Builder<?> builder) {
		return builder
				.childGroup(childGroup)
				.parentGroup(parentGroup)
				.serverContext(serverContext)
				.build();
	}
	
	private EzyHandlerGroupManager newHandlerGroupManager(
			ExecutorService statsThreadPool,
			EzySocketRequestQueues requestQueues,
			EzyHandlerGroupBuilderFactory handlerGroupBuilderFactory) {
		
		return EzyHandlerGroupManagerImpl.builder()
				.serverContext(serverContext)
				.requestQueues(requestQueues)
				.statsThreadPool(statsThreadPool)
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
	
	private EzySocketRequestQueues newRequestQueues() {
		return new EzySimpleSocketRequestQueues();
	}

	private EzySessionTicketsQueue newSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}

	private EzySessionTicketsQueue newWebSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}

	private ExecutorService newStatsThreadPool() {
		return EzyExecutors.newFixedThreadPool(EzyNettyThreadPoolSizes.STATISTICS, "statistics");
	}
	
	protected EventLoopGroup newParentEventLoopGroup() {
		return new NioEventLoopGroup(EzyNettyThreadPoolSizes.PARENT_EVENT_LOOP_GROUP, EzyExecutors.newThreadFactory("parent-event-loop-group"));
	}

	protected EventLoopGroup newChildEventLoopGroup() {
		return new NioEventLoopGroup(EzyNettyThreadPoolSizes.SOCKET_EVENT_LOOP_GROUP, EzyExecutors.newThreadFactory("socket-event-loop-group"));
	}
}
	
