package com.tvd12.ezyfoxserver.netty.builder.impl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;

class EzyWsSecureChannelInitializer extends EzyWsChannelInitializer {

	private SSLContext sslContext;

	protected EzyWsSecureChannelInitializer(Builder builder) {
		super(builder);
		this.sslContext = builder.sslContext;
	}

	@Override
	protected void initChannel(Channel ch, 
			ChannelOutboundHandler encoder, 
			ChannelInboundHandler decoder) throws Exception {
		super.initChannel(ch, encoder, decoder);
		ChannelPipeline pipeline = ch.pipeline();
		SSLEngine engine = sslContext.createSSLEngine();
		engine.setUseClientMode(false);
		pipeline.addFirst("ssl-handler", new SslHandler(engine));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyWsChannelInitializer.Builder {
		private SSLContext sslContext;

		public Builder sslContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}

		@Override
		public EzyWsChannelInitializer build() {
			return new EzyWsSecureChannelInitializer(this);
		}
	}
}
