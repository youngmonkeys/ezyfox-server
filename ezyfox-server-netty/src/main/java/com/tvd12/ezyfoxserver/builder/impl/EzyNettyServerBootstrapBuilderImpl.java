package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.EzyNettyServerBootstrap;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyAbtractServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.builder.EzyNettyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.reflect.EzyClassUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class EzyNettyServerBootstrapBuilderImpl 
		extends EzyAbtractServerBootstrapBuilder 
		implements EzyNettyServerBootstrapBuilder {

	private EventLoopGroup childGroup;
	private EventLoopGroup parentGroup;
	
	@Override
	protected EzyServerBootstrap build(EzyServerContext ctx) {
		prebuild(ctx);
		return doBuild(ctx);
	}
	
	protected void prebuild(EzyServerContext ctx) {
		this.childGroup = newChildEventLoopGroup();
		this.parentGroup = newParentEventLoopGroup();
	}
	
	protected EzyServerBootstrap doBuild(EzyServerContext ctx) {
		EzyNettyServerBootstrap answer = new EzyNettyServerBootstrap();
		answer.setContext(ctx);
		answer.setChildGroup(newParentEventLoopGroup());
		answer.setParentGroup(newChildEventLoopGroup());
		answer.setLocalBootstrap(newLocalBoostrap(ctx));
		answer.setServerBootstrap(createServerBootstrap(ctx));
		answer.setWsServerBootstrap(createWsServerBootstrap(ctx));
		return answer;
	}
	
	protected EzyServerContext newContext() {
		return EzyContextBuilderImpl.builder().boss(boss).build();
	}
	
	protected ServerBootstrap createServerBootstrap(EzyServerContext ctx) {
		return createServerBootstrap(newServerBootstrapCreator(), ctx);
	}
	
	protected ServerBootstrap createWsServerBootstrap(EzyServerContext ctx) {
		return createServerBootstrap(newWsServerBootstrapCreator(), ctx);
	}
	
	protected EzyServerBootstrapCreator<?> newServerBootstrapCreator() {
		return EzyServerBootstrapCreator.newInstance()
				.port(port)
				.codecCreator(newCodecCreator());
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsServerBootstrapCreator() {
		return EzyWsServerBootstrapCreator.newInstance()
				.port(wsport)
				.codecCreator(newWsCodecCreator());
	}
	
	protected SslContext createWsSslContext()  {
		try {
			return tryCreateWsSslContext();
		}
		catch(Exception e) {
			throw new IllegalStateException("can not create ssl context", e);
		}
	}
	
	protected SslContext tryCreateWsSslContext() throws Exception {
		SelfSignedCertificate cert = new SelfSignedCertificate();
		return SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
	}
	
	protected ServerBootstrap createServerBootstrap(
			EzyAbstractBootstrapCreator<?> creator, EzyServerContext ctx) {
		return creator
				.context(ctx)
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
    	return EzyClassUtil.newInstance(getCodecCreatorClassName());
    }
    
    protected EzyCodecCreator newWsCodecCreator() {
    	return EzyClassUtil.newInstance(getWsCodecCreatorClassName());
    }
    
    protected String getCodecCreatorClassName() {
    	return "com.tvd12.ezyfoxserver.codec.MsgPackCodecCreator";
    }
    
    protected String getWsCodecCreatorClassName() {
    	return "com.tvd12.ezyfoxserver.codec.JacksonCodecCreator";
    }
	
}
