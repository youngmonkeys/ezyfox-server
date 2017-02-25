package com.tvd12.ezyfoxserver.codec;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;

@Builder
@SuppressWarnings("rawtypes")
public class MessageToBytesImpl implements EzyMessageToBytes {

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
		answer.put(ByteBuf.class, new Converter<ByteBuf>() {
			@Override
			public ByteBuf convert(EzyMessage message) {
				return 	Unpooled
						.buffer(4 + message.getSize())
						.writeInt(message.getSize())
						.writeBytes(message.getContent());
			}
		});
		return answer;
	}

}

interface Converter<T> {
	T convert(EzyMessage message);
}