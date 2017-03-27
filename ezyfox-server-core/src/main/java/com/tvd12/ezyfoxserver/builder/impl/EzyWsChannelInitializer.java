package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.handler.TextWebSocketFrameHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

class EzyWsChannelInitializer extends ChannelInitializer<Channel> {

	protected EzyWsChannelInitializer(Builder builder) {
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler());
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		public EzyWsChannelInitializer build() {
			return new EzyWsChannelInitializer(this);
		}

	}
}