package com.tvd12.ezyfoxserver.netty.builder.impl;

import javax.net.ssl.SSLContext;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;

public class EzyWsServerBootstrapCreatorFactory {

	protected int port;
	protected int sslPort;
	protected int maxFrameSize;
	protected boolean sslActive;
	protected SSLContext sslContext;
	protected EzyCodecCreator wsCodecCreator;
	
	protected EzyWsServerBootstrapCreatorFactory(Builder builder) {
		this.port = builder.port;
		this.sslPort = builder.sslPort;
		this.sslActive = builder.sslActive;
		this.sslContext = builder.sslContext;
		this.maxFrameSize = builder.maxFrameSize;
		this.wsCodecCreator = builder.wsCodecCreator;
	}
	
	public EzyWsServerBootstrapCreator<?> newCreator() {
		return sslActive
			? newWsServerBootstrapCreator(newWsSecureServerBootstrapCreator())
			: newWsServerBootstrapCreator(newWsServerBootstrapCreator());
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsServerBootstrapCreator(
			EzyWsServerBootstrapCreator<?> creator) {
		return creator
				.port(getPort())
				.maxFrameSize(maxFrameSize)
				.maxRequestSize(maxFrameSize)
				.codecCreator(wsCodecCreator);
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsServerBootstrapCreator() {
		return EzyWsServerBootstrapCreator.newInstance();
	}
	
	protected EzyWsServerBootstrapCreator<?> newWsSecureServerBootstrapCreator() {
		return EzyWsSecureServerBootstrapCreator.newInstance()
				.sslContext(sslContext);
	}
	
	protected int getPort() {
		return sslActive ? sslPort : port;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder 
			implements EzyBuilder<EzyWsServerBootstrapCreatorFactory> {
		
		protected int port;
		protected int sslPort;
		protected int maxFrameSize;
		protected boolean sslActive;
		protected SSLContext sslContext;
		protected EzyCodecCreator wsCodecCreator;
		
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		
		public Builder sslPort(int sslPort) {
			this.sslPort = sslPort;
			return this;
		}
		
		public Builder maxFrameSize(int maxFrameSize) {
			this.maxFrameSize = maxFrameSize;
			return this;
		}
		
		public Builder sslActive(boolean sslActive) {
			this.sslActive = sslActive;
			return this;
		}
		
		public Builder sslContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			return this;
		}
		
		public Builder wsCodecCreator(EzyCodecCreator wsCodecCreator) {
			this.wsCodecCreator = wsCodecCreator;
			return this;
		}
		
		@Override
		public EzyWsServerBootstrapCreatorFactory build() {
			return new EzyWsServerBootstrapCreatorFactory(this);
		}
	}
}
