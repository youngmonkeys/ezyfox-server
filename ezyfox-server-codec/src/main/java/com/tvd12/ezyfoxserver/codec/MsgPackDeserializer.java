package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;

public interface MsgPackDeserializer {
	
	<T> T deserialize(final byte[] data);
	
	<T> T deserialize(final ByteBuffer buffer);
	
}
