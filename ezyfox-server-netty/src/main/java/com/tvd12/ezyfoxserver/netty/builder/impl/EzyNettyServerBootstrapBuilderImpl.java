package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.builder.EzyHttpServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.netty.EzyNettyServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzySocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.EzyWebSocketServerBootstrap;
import com.tvd12.ezyfoxserver.netty.builder.EzyNettyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

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
		EzyNettyServerBootstrap answer = new EzyNettyServerBootstrap();
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
		answer.setSocketServerBootstrap(createSocketServerBootstrap());
		answer.setWebsocketServerBootstrap(createWebSocketServerBootstrap());
		return answer;
	}
	
	protected EzySocketServerBootstrap createSocketServerBootstrap() {
		return createSocketServerBootstrap(EzySocketServerBootstrap.builder());
	}
	
	protected EzySocketServerBootstrap createWebSocketServerBootstrap() {
		return createSocketServerBootstrap(EzyWebSocketServerBootstrap.builder()
				.sslContextSupplier(this::newSslContext));
	}
	
	protected EzySocketServerBootstrap createSocketServerBootstrap(
			EzySocketServerBootstrap.Builder<?> builder) {
		return builder
				.childGroup(childGroup)
				.parentGroup(parentGroup)
				.serverContext(serverContext)
				.build();
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
