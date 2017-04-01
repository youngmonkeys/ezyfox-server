package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.handler.EzyWsFrameHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

class EzyWsChannelInitializer extends EzyChannelInitializer {

	protected EzyWsChannelInitializer(Builder builder) {
		super(builder);
	}

	@Override
	protected void initChannel(Channel ch, 
			ChannelOutboundHandler encoder, 
			ChannelInboundHandler decoder) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new EzyWsFrameHandler());
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast(newDataHandler());
		pipeline.addLast(new EzyCombinedCodec(decoder, encoder));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyChannelInitializer.Builder {

		public EzyWsChannelInitializer build() {
			return new EzyWsChannelInitializer(this);
		}

	}
}