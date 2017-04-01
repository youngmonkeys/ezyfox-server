package com.tvd12.ezyfoxserver.builder.impl;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

class EzyWsSecureChannelInitializer extends EzyWsChannelInitializer {

	private SslContext sslContext;

	protected EzyWsSecureChannelInitializer(Builder builder) {
		super(builder);
		this.sslContext = builder.sslContext;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		super.initChannel(ch);
		SSLEngine engine = sslContext.newEngine(ch.alloc());
		ch.pipeline().addFirst(new SslHandler(engine));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyWsChannelInitializer.Builder {
		private SslContext sslContext;

		public Builder sslContext(SslContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}

		@Override
		public EzyWsChannelInitializer build() {
			return new EzyWsSecureChannelInitializer(this);
		}
	}

}
