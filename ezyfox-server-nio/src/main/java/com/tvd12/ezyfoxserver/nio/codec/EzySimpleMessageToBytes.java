package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.codec.EzyMessage;
import com.tvd12.ezyfoxserver.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.codec.EzyMessageToBytes;

import lombok.Builder;

@Builder
public class EzySimpleMessageToBytes implements EzyMessageToBytes {

	@SuppressWarnings("unchecked")
	@Override
	public ByteBuffer convert(EzyMessage message) {
		ByteBuffer answer = newByteBuffer(message);
		writeHeader(answer, message);
		writeSize(answer, message);
		writeContent(answer, message);
		answer.flip();
		return answer;
	}
	
	private void writeHeader(ByteBuffer answer, EzyMessage message) {
		writeHeader(answer, message.getHeader());
	}
	
	private void writeHeader(ByteBuffer answer, EzyMessageHeader header) {
		byte headerByte = 0;
		headerByte |= header.isBigSize() ? 1 << 0 : 0;
		headerByte |= header.isEncrypted() ? 1 << 1 : 0;
		headerByte |= header.isCompressed() ? 1 << 2 : 0;
		headerByte |= header.isText() ? 1 << 3 : 0;
		answer.put(headerByte);
	}
	
	private void writeSize(ByteBuffer answer, EzyMessage message) {
		if(message.hasBigSize())
			answer.putInt(message.getSize());
		else
			answer.putShort((short)message.getSize());
	}
	
	private void writeContent(ByteBuffer answer, EzyMessage message) {
		answer.put(message.getContent());
	}
	
	private ByteBuffer newByteBuffer(EzyMessage message) {
		int capacity = getCapacity(message);
		return ByteBuffer.allocate(capacity);
	}
	
	private int getCapacity(EzyMessage message){
		return 1 + message.getSizeLength() + message.getContent().length;
	}
	
}