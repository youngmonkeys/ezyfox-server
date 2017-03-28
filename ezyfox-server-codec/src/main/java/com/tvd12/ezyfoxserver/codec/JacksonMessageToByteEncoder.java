package com.tvd12.ezyfoxserver.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class JacksonMessageToByteEncoder extends MessageToByteEncoder<EzyArray> {

	protected final Logger logger;
	protected final EzyMessageSerializer serializer;
	
	public JacksonMessageToByteEncoder(EzyMessageSerializer serializer) {
		this.serializer = serializer;
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, EzyArray msg, ByteBuf out) 
			throws Exception {
		writeMessage(serializer.serialize(msg), out);
	}
	
	private void writeMessage(byte[] message, ByteBuf out) {
		logger.debug("write message with size {}", message.length);
		out.writeBytes(message);
	}

}
