package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;

import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.entity.EzyObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

public class MsgPackMessageToByteEncoder extends MessageToByteEncoder<EzyObject> {

	private Logger logger;
	private MessagePack msgPack;
	
	public MsgPackMessageToByteEncoder(MessagePack msgPack) {
		this.msgPack = msgPack;
		this.logger = LoggerFactory.getLogger(getClass());
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, 
			EzyObject msg, ByteBuf out) throws Exception {
		try {
			byte[] content = msgPack.write(msg);
			ByteBuffer buffer = ByteBuffer.allocate(4 + content.length)
				.putInt(content.length)
				.put(content);
			buffer.flip();
			out.writeBytes(buffer);
			System.out.println("MsgPackMessageToByteEncoder#encode end length = " + (4 + content.length));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
