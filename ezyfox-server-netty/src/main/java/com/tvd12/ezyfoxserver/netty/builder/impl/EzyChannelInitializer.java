package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.netty.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.netty.creator.EzyDataHandlerCreator;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;

class EzyChannelInitializer extends ChannelInitializer<Channel> {
	
	protected int maxRequestSize;
	protected EzyCodecCreator codecCreator;
	protected EzyDataHandlerCreator dataHandlerCreator;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	
	protected EzyChannelInitializer(Builder builder) {
		this.codecCreator = builder.codecCreator;
		this.maxRequestSize = builder.maxRequestSize;
		this.dataHandlerCreator = builder.dataHandlerCreator;
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		initChannel(ch, newEncoder(), newDecoder());
	}
	
	protected void initChannel(Channel ch, 
			ChannelOutboundHandler encoder, 
			ChannelInboundHandler decoder) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("codec-1", new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast("handler", newDataHandler(ch));
		pipeline.addLast("codec-2", new EzyCombinedCodec(decoder, encoder));
	}
	
	protected ChannelHandler newDataHandler(Channel ch) {
		return dataHandlerCreator.newHandler(ch, EzyConnectionType.SOCKET);
	}
	
	protected ChannelOutboundHandler newEncoder() {
		return (ChannelOutboundHandler) codecCreator.newEncoder();
	}
	
	protected ChannelInboundHandler newDecoder() {
		return (ChannelInboundHandler) codecCreator.newDecoder(maxRequestSize);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		protected int maxRequestSize;
		private EzyCodecCreator codecCreator;
		private EzyDataHandlerCreator dataHandlerCreator;
		
		public Builder maxRequestSize(int maxRequestSize) {
			this.maxRequestSize = maxRequestSize;
			return this;
		}
		
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