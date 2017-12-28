package com.tvd12.ezyfoxserver.netty.builder.impl;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.netty.codec.EzyCombinedCodec;
import com.tvd12.ezyfoxserver.netty.handler.EzyHttpRequestHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

class EzyWsChannelInitializer extends EzyChannelInitializer {

	protected int maxFrameSize;
	
	protected EzyWsChannelInitializer(Builder builder) {
		super(builder);
		this.maxFrameSize = builder.maxFrameSize;
	}
	
	@Override
	protected void initChannel(Channel ch, 
			ChannelOutboundHandler encoder, 
			ChannelInboundHandler decoder) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("http-server-codec", new HttpServerCodec());
		pipeline.addLast("http-object-aggregator", new HttpObjectAggregator(64 * 1024));
		pipeline.addLast("chunked-write-andler", new ChunkedWriteHandler());
		pipeline.addLast("http-request-handler", new EzyHttpRequestHandler("/ws"));
		pipeline.addLast("ws-server-protocol-handler", newWebSocketServerProtocolHandler());
		pipeline.addLast("codec-1", new EzyCombinedCodec(decoder, encoder));
		pipeline.addLast("handler", newDataHandler(ch));
		pipeline.addLast("codec-2", new EzyCombinedCodec(decoder, encoder));
	}
	
	@Override
	protected ChannelHandler newDataHandler(Channel ch) {
		return dataHandlerCreator.newHandler(ch, EzyConnectionType.WEBSOCKET);
	}
	
	protected WebSocketServerProtocolHandler newWebSocketServerProtocolHandler() {
		return new WebSocketServerProtocolHandler("/ws", null, false, maxFrameSize) {
			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
				if(cause instanceof CorruptedFrameException)
					ctx.fireExceptionCaught(new EzyMaxRequestSizeException(cause.getMessage()));
				else
					super.exceptionCaught(ctx, cause);
			}
		};
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyChannelInitializer.Builder {

		protected int maxFrameSize;
		
		public Builder maxFrameSize(int maxFrameSize) {
			this.maxFrameSize = maxFrameSize;
			return this;
		}
		
		public EzyWsChannelInitializer build() {
			return new EzyWsChannelInitializer(this);
		}

	}
}