package com.tvd12.ezyfoxserver.codec;

public interface EzyMessageToBytes {

	<T> T convert(EzyMessage message);
	
}
