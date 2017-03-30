package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;

public interface EzyObjectDeserializer {
	
	<T> T deserialize(byte[] data);
	
	<T> T deserialize(ByteBuffer buffer);
	
}
