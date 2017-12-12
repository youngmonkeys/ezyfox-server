package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.netty.EzyNettyServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.builder.EzyNettyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettySocketWriter;
import com.tvd12.ezyfoxserver.netty.socket.EzyNettySocketWritingLoopHandler;
import com.tvd12.ezyfoxserver.netty.websocket.EzyWsWritingLoopHandler;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EzyNettyServerBootstrapBuilderImpl 
		extends EzyHttpServerBootstrapBuilder 
		implements EzyNettyServerBootstrapBuilder {

	private EventLoopGroup childGroup;
	private EventLoopGroup parentGroup;
	
	@Override
	protected void prebuild() {
		childGroup = newChildEventLoopGroup();
		parentGroup = newParentEventLoopGroup();
	}
	
	@Override
	protected EzyNettyServerBootstrap newServerBootstrap() {
		EzyNettyServerBootstrap answer = new EzyNettyServerBootstrap();
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
		answer.setSocketServerBootstrap(createSocketServerBootstrap());
		answer.setWebsocketServerBootstrap(createWebSocketServerBootstrap());
		return answer;
	}
	
	protected EzySocketServerBootstrap createSocketServerBootstrap() {
		EzySessionTicketsQueue sessionTicketsQueue = newSocketSessionTicketsQueue();
		EzyServerBootstrapCreator<?> creator = newServerBootstrapCreator();
		creator.sessionTicketsQueue(sessionTicketsQueue);
		ServerBootstrap bootstrap = createNettyServerBootstrap(creator);
		EzySocketServerBootstrap serverBootstrap = new EzySocketServerBootstrap();
		EzySocketEventLoopHandler writingLoopHandler = newSocketWritingLoopHandler(sessionTicketsQueue);
		serverBootstrap.setBootstrap(bootstrap);
		serverBootstrap.setWritingLoopHandler(writingLoopHandler);
		return serverBootstrap;
	}
	
	protected EzySocketServerBootstrap createWebSocketServerBootstrap() {
		EzySessionTicketsQueue sessionTicketsQueue = newWebSocketSessionTicketsQueue();
		EzyWsServerBootstrapCreator<?> creator = newWsServerBootstrapCreator();
		creator.sessionTicketsQueue(sessionTicketsQueue);
		ServerBootstrap bootstrap = createNettyServerBootstrap(creator);
		EzySocketServerBootstrap serverBootstrap = new EzyWebSocketServerBootstrap();
		EzySocketEventLoopHandler writingLoopHandler = newWebSocketWritingLoopHandler(sessionTicketsQueue);
		serverBootstrap.setBootstrap(bootstrap);
		serverBootstrap.setWritingLoopHandler(writingLoopHandler);
		return serverBootstrap;
	}
	
	private EzySocketEventLoopHandler newSocketWritingLoopHandler(
			EzySessionTicketsQueue sessionTicketsQueue) {
		EzySocketEventLoopHandler loopHandler = new EzyNettySocketWritingLoopHandler();
		loopHandler.setThreadPoolSize(getSocketWriterPoolSize());
		EzyNettySocketWriter eventHandler = new EzyNettySocketWriter();
		eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	private EzySocketEventLoopHandler newWebSocketWritingLoopHandler(
			EzySessionTicketsQueue sessionTicketsQueue) {
		EzySocketEventLoopHandler loopHandler = new EzyWsWritingLoopHandler();
		loopHandler.setThreadPoolSize(getWebSocketWriterPoolSize());
		EzyNettySocketWriter eventHandler = new EzyNettySocketWriter();
		eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
		loopHandler.setEventHandler(eventHandler);
		return loopHandler;
	}
	
	protected EzyServerBootstrapCreator<?> newServerBootstrapCreator() {
		return EzyServerBootstrapCreator.newInstance()
				.codecCreator(newCodecCreator())
				.port(getSettings().getSocket().getPort())
				.maxRequestSize(getSocketSettings().getMaxRequestSize());
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsServerBootstrapCreator() {
		return newWsServerBootstrapCreatorFactory().newCreator();
	}
	
	protected EzyWsServerBootstrapCreatorFactory newWsServerBootstrapCreatorFactory() {
		return EzyWsServerBootstrapCreatorFactory.builder()
				.wsCodecCreator(newWsCodecCreator())
				.port(getWebsocketSettings().getPort())
				.sslPort(getWebsocketSettings().getSslPort())
				.sslActive(getWebsocketSettings().isSslActive())
				.sslContext(newSslContext(getWebsocketSettings().getSslConfig()))
				.maxFrameSize(getWebsocketSettings().getMaxFrameSize())
				.build();
	}
	
	protected ServerBootstrap createNettyServerBootstrap(EzyAbstractBootstrapCreator<?> creator) {
		return creator
				.context(serverContext)
				.childGroup(childGroup)
				.parentGroup(parentGroup)
				.create();
	}
	
	protected EventLoopGroup newParentEventLoopGroup() {
		return new NioEventLoopGroup(0, EzyExecutors.newThreadFactory("parenteventloopgroup"));
    }
    
    protected EventLoopGroup newChildEventLoopGroup() {
    		return new NioEventLoopGroup(0, EzyExecutors.newThreadFactory("childeventloopgroup"));
    }
    
    protected EzyCodecCreator newCodecCreator() {
        return EzyClasses.newInstance(getCodecCreatorClassName());
    }
    
    protected EzyCodecCreator newWsCodecCreator() {
        return EzyClasses.newInstance(getWsCodecCreatorClassName());
    }
    
    protected EzySessionTicketsQueue newSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	protected EzySessionTicketsQueue newWebSocketSessionTicketsQueue() {
		return new EzyBlockingSessionTicketsQueue();
	}
	
	private int getSocketWriterPoolSize() {
		return 8;
	}
	
	private int getWebSocketWriterPoolSize() {
		return 8;
	}
    
}
