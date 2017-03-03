package com.tvd12.ezyfoxserver.codec;

public interface MsgPackDeserializer {
	
	<T> T deserialize(final byte[] data);
	
}
