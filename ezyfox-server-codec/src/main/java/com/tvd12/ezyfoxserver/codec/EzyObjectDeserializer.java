package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.io.EzyStrings;

public interface EzyObjectDeserializer {
	
	<T> T deserialize(ByteBuffer buffer);
	
	default <T> T deserialize(byte[] data) {
		return deserialize(ByteBuffer.wrap(data));
	}
	
	default <T> T deserialize(String text) {
		return deserialize(EzyStrings.getUtfBytes(text));
	}
	
}
