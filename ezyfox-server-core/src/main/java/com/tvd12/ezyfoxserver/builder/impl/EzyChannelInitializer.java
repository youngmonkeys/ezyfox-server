package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.creator.EzyDataHandlerCreator;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;

class EzyChannelInitializer extends ChannelInitializer<Channel> {
	
	private EzyCodecCreator codecCreator;
	private EzyDataHandlerCreator dataHandlerCreator;
	
	protected EzyChannelInitializer(Builder builder) {
		this.codecCreator = builder.codecCreator;
		this.dataHandlerCreator = builder.dataHandlerCreator;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		ChannelOutboundHandler encoder = codecCreator.newEncoder();
		ChannelInboundHandlerAdapter decoder = codecCreator.newDecoder();
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast(dataHandlerCreator.newHandler());
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private EzyCodecCreator codecCreator;
		private EzyDataHandlerCreator dataHandlerCreator;
		
		public Builder codecCreator(EzyCodecCreator codecCreator) {
			this.codecCreator = codecCreator;
			return this;
		}
		
		public Builder dataHandlerCreator(EzyDataHandlerCreator dataHandlerCreator) {
			this.dataHandlerCreator = dataHandlerCreator;
			return this;
		}
		
		public EzyChannelInitializer build() {
			return new EzyChannelInitializer(this);
		}
		
	}
}