package com.tvd12.ezyfoxserver.netty.codec;

import java.util.List;

import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class JacksonByteToMessageDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

	private final EzyMessageDeserializer deserializer;
	
	public JacksonByteToMessageDecoder(EzyMessageDeserializer deserializer) {
		this.deserializer = deserializer;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame in, List<Object> out) 
			throws Exception {
		String text = in.text();
		Object value = deserializer.deserialize(text);
		out.add(value);
	}
}
