package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.netty.EzyNettyServerBootstrap;
import com.tvd12.ezyfoxserver.netty.builder.EzyNettyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

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
		answer.setServerBootstrap(createServerBootstrap());
		answer.setWsServerBootstrap(createWsServerBootstrap());
		return answer;
	}
	
	protected ServerBootstrap createServerBootstrap() {
		return createServerBootstrap(newServerBootstrapCreator());
	}
	
	protected ServerBootstrap createWsServerBootstrap() {
		return createServerBootstrap(newWsServerBootstrapCreator());
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
	
	protected ServerBootstrap createServerBootstrap(EzyAbstractBootstrapCreator<?> creator) {
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
    
}
