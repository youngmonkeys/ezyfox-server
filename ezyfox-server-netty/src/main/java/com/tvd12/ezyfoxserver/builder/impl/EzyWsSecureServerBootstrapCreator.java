package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.builder.impl.EzyWsChannelInitializer.Builder;

import io.netty.handler.ssl.SslContext;

public class EzyWsSecureServerBootstrapCreator 
		extends EzyWsServerBootstrapCreator<EzyWsSecureServerBootstrapCreator> {

	private SslContext sslContext;
	
	public static EzyWsSecureServerBootstrapCreator newInstance() {
		return new EzyWsSecureServerBootstrapCreator();
	}
	
	public EzyWsSecureServerBootstrapCreator sslContext(SslContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}
	
	@Override
	protected Builder newWsChannelInitializerBuilder() {
		return EzyWsSecureChannelInitializer.builder()
				.sslContext(sslContext);
	}
}
