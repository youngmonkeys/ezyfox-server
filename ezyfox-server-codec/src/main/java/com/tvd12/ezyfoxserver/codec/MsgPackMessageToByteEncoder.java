package com.tvd12.ezyfoxserver.codec;

import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class MsgPackMessageToByteEncoder extends MessageToByteEncoder<EzyObject> {

	private Logger logger;
	private EzyMessageToBytes messageToBytes;
	private EzyObjectToMessage objectToMessage;

	public MsgPackMessageToByteEncoder(MessagePack msgPack) {
		this.logger = newLogger();
		this.messageToBytes = newMessageToBytes();
		this.objectToMessage = newObjectToMessage(msgPack);
	}
	
	private Logger newLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	private EzyObjectToMessage newObjectToMessage(MessagePack msgPack) {
		return MsgPackObjectToMessage.builder().msgPack(msgPack).build();
	}
	
	private EzyMessageToBytes newMessageToBytes() {
		return EzySimpleMessageToBytes.builder().build();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, EzyObject msg, ByteBuf out) 
			throws Exception {
		writeMessage(convertObjectToBytes(msg), out);
	}
	
	private ByteBuf convertObjectToBytes(EzyObject object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	private EzyMessage convertObjectToMessage(EzyObject object) {
		return objectToMessage.convert(object);
	}
	
	private ByteBuf convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message, ByteBuf.class);
	}
	
	private void writeMessage(ByteBuf message, ByteBuf out) {
		logger.info("write message with size {}", message.readableBytes());
		out.writeBytes(message);
		ReferenceCountUtil.release(message);
	}

}
