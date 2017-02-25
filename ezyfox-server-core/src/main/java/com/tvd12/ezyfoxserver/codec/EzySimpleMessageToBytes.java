package com.tvd12.ezyfoxserver.codec;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;

@Builder
@SuppressWarnings("rawtypes")
public class EzySimpleMessageToBytes implements EzyMessageToBytes {

	private static final Map<Class, Converter> CONVERTERS;
	
	static {
		CONVERTERS = defaultConverters();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(EzyMessage message, Class<T> type) {
		return (T) getConverters().get(type).convert(message);
	}
	
	protected Map<Class, Converter> getConverters() {
		return CONVERTERS;
	}
	
	private static Map<Class, Converter> defaultConverters() {
		Map<Class, Converter> answer = new HashMap<>();
		answer.put(ByteBuf.class, new MessageToByteBuf());
		return answer;
	}

}

interface Converter<T> {
	T convert(EzyMessage message);
}

class MessageToByteBuf implements Converter<ByteBuf> {

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
		headerByte |= header.isBigSize() ? 1 : 0;
		headerByte |= header.isEncrypted() ? 1 << 1 : 0;
		headerByte |= header.isCompressed() ? 1 << 2 : 0;
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
		return 1 /*header size*/ + message.getSizeLength() + message.getContent().length;
	}
	
}