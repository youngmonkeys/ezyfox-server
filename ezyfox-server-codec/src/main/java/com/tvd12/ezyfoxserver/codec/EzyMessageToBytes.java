package com.tvd12.ezyfoxserver.codec;

public interface EzyMessageToBytes {

	<T> T convert(final EzyMessage message, final Class<T> type);
	
}
