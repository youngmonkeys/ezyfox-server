package com.tvd12.ezyfoxserver.netty.codec;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.codec.EzyMessageToBytes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;

@Builder
public class EzySimpleMessageToBytes implements EzyMessageToBytes {

	@SuppressWarnings("unchecked")
	@Override
	public ByteBuf convert(EzyMessage message) {
		ByteBuf answer = newByteBuf(message);
		writeHeader(answer, message);
		writeSize(answer, message);
		writeContent(answer, message);
		return answer;
	}
	
	private void writeHeader(ByteBuf answer, EzyMessage message) {
		writeHeader(answer, message.getHeader());
	}
	
	private void writeHeader(ByteBuf answer, EzyMessageHeader header) {
		byte headerByte = 0;
		headerByte |= header.isBigSize() ? 1 << 0 : 0;
		headerByte |= header.isEncrypted() ? 1 << 1 : 0;
		headerByte |= header.isCompressed() ? 1 << 2 : 0;
		headerByte |= header.isText() ? 1 << 3 : 0;
		answer.writeByte(headerByte);
	}
	
	private void writeSize(ByteBuf answer, EzyMessage message) {
		if(message.hasBigSize())
			answer.writeInt(message.getSize());
		else
			answer.writeShort(message.getSize());
	}
	
	private void writeContent(ByteBuf answer, EzyMessage message) {
		answer.writeBytes(message.getContent());
	}
	
	private ByteBuf newByteBuf(EzyMessage message) {
		return Unpooled.buffer(getCapacity(message));
	}
	
	private int getCapacity(EzyMessage message){
		return 1 + message.getSizeLength() + message.getContent().length;
	}
	
}