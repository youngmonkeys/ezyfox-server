package com.tvd12.ezyfoxserver.codec;

import java.util.Map;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MsgPackMessageToByteEncoder extends MessageToByteEncoder<Map<String, String>> {

	private MessagePack msgPack;
	
	@Override
	protected void encode(ChannelHandlerContext ctx, 
			Map<String, String> msg, ByteBuf out) throws Exception {
		out.writeBytes(msgPack.write(msg));
	}

}
