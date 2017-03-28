package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class EzyServerBootstrapBuilderImpl implements EzyServerBootstrapBuilder {

	private int port;
	private int wsport;
	private EzyServer boss;
	private EventLoopGroup childGroup;
	private EventLoopGroup parentGroup;
	private EzyCodecCreator codecCreator;
	private EzyCodecCreator wsCodecCreator;
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#port(int)
	 */
	@Override
	public EzyServerBootstrapBuilder port(int port) {
		this.port = port;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#wsport(int)
	 */
	@Override
	public EzyServerBootstrapBuilder wsport(int wsport) {
		this.wsport = wsport;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#boss(com.tvd12.ezyfoxserver.EzyServer)
	 */
	@Override
	public EzyServerBootstrapBuilder boss(EzyServer boss) {
		this.boss = boss;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#childGroup(io.netty.channel.EventLoopGroup)
	 */
	@Override
	public EzyServerBootstrapBuilder childGroup(EventLoopGroup childGroup) {
		this.childGroup = childGroup;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#parentGroup(io.netty.channel.EventLoopGroup)
	 */
	@Override
	public EzyServerBootstrapBuilder parentGroup(EventLoopGroup parentGroup) {
		this.parentGroup = parentGroup;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#codecCreator(com.tvd12.ezyfoxserver.codec.EzyCodecCreator)
	 */
	@Override
	public EzyServerBootstrapBuilder codecCreator(EzyCodecCreator codecCreator) {
		this.codecCreator = codecCreator;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#wsCodecCreator(com.tvd12.ezyfoxserver.codec.EzyCodecCreator)
	 */
	@Override
	public EzyServerBootstrapBuilder wsCodecCreator(EzyCodecCreator wsCodecCreator) {
		this.wsCodecCreator = wsCodecCreator;
		return this;
	}
	
	protected EzyBootstrap newLocalBoostrap(EzyServerContext context) {
    	return EzyBootstrap.builder().context(context).build();
    }
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyBuilder#build()
	 */
	@Override
	public EzyServerBootstrap build() {
		return build(newContext());
	}
	
	protected EzyServerBootstrap build(EzyServerContext ctx) {
		EzyServerBootstrap answer = new EzyServerBootstrap();
		answer.setContext(ctx);
		answer.setChildGroup(childGroup);
		answer.setParentGroup(parentGroup);
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
				.codecCreator(codecCreator);
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsServerBootstrapCreator() {
		return EzyWsServerBootstrapCreator.newInstance()
				.port(wsport)
				.codecCreator(wsCodecCreator);
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
}


