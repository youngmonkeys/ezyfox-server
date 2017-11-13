package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.handler.EzyClientHandler;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.netty.codec.EzyCombinedCodec;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;

class EzyClientChannelInitializer extends ChannelInitializer<Channel> {

	protected EzyClientContext context;
	protected EzyCodecCreator codecCreator;
	
	protected EzyClientChannelInitializer(Builder builder) {
		this.context = builder.context;
		this.codecCreator = builder.codecCreator;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ChannelOutboundHandler encoder = (ChannelOutboundHandler) codecCreator.newEncoder();
		ChannelInboundHandlerAdapter decoder = (ChannelInboundHandlerAdapter) codecCreator.newDecoder(65536);
		pipeline.addLast("codec-1", new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast("handler", newDataHandler());
		pipeline.addLast("codec-2", new EzyCombinedCodec(decoder, encoder));
	}
	
	protected EzyClientHandler newDataHandler() {
		EzyClientHandler handler = new EzyClientHandler();
		handler.setContext(context);
		return handler;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyClientChannelInitializer> {
		protected EzyClientContext context;
		protected EzyCodecCreator codecCreator;
		
		public Builder context(EzyClientContext context) {
			this.context = context;
			return this;
		}
		
		public Builder codecCreator(EzyCodecCreator codecCreator) {
			this.codecCreator = codecCreator;
			return this;
		}
		
		@Override
		public EzyClientChannelInitializer build() {
			return new EzyClientChannelInitializer(this);
		}
	    
	}
 }