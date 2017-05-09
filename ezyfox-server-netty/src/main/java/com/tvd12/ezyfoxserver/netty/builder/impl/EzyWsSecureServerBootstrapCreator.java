package com.tvd12.ezyfoxserver.netty.builder.impl;

import javax.net.ssl.SSLContext;

import com.tvd12.ezyfoxserver.netty.builder.impl.EzyWsChannelInitializer.Builder;

public class EzyWsSecureServerBootstrapCreator 
		extends EzyWsServerBootstrapCreator<EzyWsSecureServerBootstrapCreator> {

	private SSLContext sslContext;
	
	public static EzyWsSecureServerBootstrapCreator newInstance() {
		return new EzyWsSecureServerBootstrapCreator();
	}
	
	public EzyWsSecureServerBootstrapCreator sslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}
	
	@Override
	protected Builder newWsChannelInitializerBuilder() {
		return EzyWsSecureChannelInitializer.builder()
				.sslContext(sslContext);
	}
}
