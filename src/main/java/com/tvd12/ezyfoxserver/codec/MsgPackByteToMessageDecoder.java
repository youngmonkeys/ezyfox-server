package com.tvd12.ezyfoxserver.codec;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MsgPackByteToMessageDecoder extends ByteToMessageDecoder {

	private MessagePack msgPack;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes, 0, in.readableBytes());
		out.add(msgPack.read(bytes));
	}
	
	

}
