package com.tvd12.ezyfoxserver.netty.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.netty.io.EzyByteBufs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class JacksonByteToMessageDecoder extends ByteToMessageDecoder {

	private final Logger logger;
	private final EzyMessageDeserializer deserializer;
	
	public JacksonByteToMessageDecoder(EzyMessageDeserializer deserializer) {
		this.deserializer = deserializer;
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) 
			throws Exception {
		logger.debug("decode {} bytes", in);
		out.add(deserializer.deserialize(EzyByteBufs.getBytes(in)));
	}
}
