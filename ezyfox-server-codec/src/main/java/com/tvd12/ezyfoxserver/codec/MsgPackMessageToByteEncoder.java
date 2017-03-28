package com.tvd12.ezyfoxserver.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class MsgPackMessageToByteEncoder extends MessageToByteEncoder<EzyArray> {

	protected final Logger logger;
	protected final EzyMessageToBytes messageToBytes;
	protected final EzyObjectToMessage objectToMessage;
	
	public MsgPackMessageToByteEncoder(
			EzyMessageToBytes messageToBytes,
			EzyObjectToMessage objectToMessage) {
		this.messageToBytes = messageToBytes;
		this.objectToMessage = objectToMessage;
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, EzyArray msg, ByteBuf out) 
			throws Exception {
		writeMessage(convertObjectToBytes(msg), out);
	}
	
	protected ByteBuf convertObjectToBytes(EzyArray object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	protected EzyMessage convertObjectToMessage(EzyArray object) {
		return objectToMessage.convert(object);
	}
	
	protected ByteBuf convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message, ByteBuf.class);
	}
	
	protected void writeMessage(ByteBuf message, ByteBuf out) {
		logger.debug("write message with size {}", message.readableBytes());
		out.writeBytes(message);
		ReferenceCountUtil.release(message);
	}

}
