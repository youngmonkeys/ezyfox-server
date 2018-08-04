package com.tvd12.ezyfoxserver.netty.codec;

import com.tvd12.ezyfox.codec.EzyMessage;
import com.tvd12.ezyfox.codec.EzyMessageToBytes;
import com.tvd12.ezyfox.codec.EzyObjectToMessage;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.handler.EzyBytesSent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class MsgPackMessageToByteEncoder extends MessageToByteEncoder<EzyArray> {

	protected final EzyMessageToBytes messageToBytes;
	protected final EzyObjectToMessage objectToMessage;
	
	public MsgPackMessageToByteEncoder(
			EzyMessageToBytes messageToBytes,
			EzyObjectToMessage objectToMessage) {
		this.messageToBytes = messageToBytes;
		this.objectToMessage = objectToMessage;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, EzyArray msg, ByteBuf out) 
			throws Exception {
		ByteBuf bytes = convertObjectToBytes(msg);
		int bytesCount = bytes.readableBytes();
		writeMessage(bytes, out);
		EzyBytesSent delegate = (EzyBytesSent)ctx.pipeline().get("handler");
		delegate.bytesSent(bytesCount);
	}
	
	protected ByteBuf convertObjectToBytes(EzyArray object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	protected EzyMessage convertObjectToMessage(EzyArray object) {
		return objectToMessage.convert(object);
	}
	
	protected ByteBuf convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message);
	}
	
	protected void writeMessage(ByteBuf message, ByteBuf out) {
		out.writeBytes(message);
		ReferenceCountUtil.release(message);
	}

}
