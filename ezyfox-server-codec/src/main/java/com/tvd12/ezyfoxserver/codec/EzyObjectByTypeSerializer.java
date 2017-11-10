package com.tvd12.ezyfoxserver.codec;

public interface EzyObjectByTypeSerializer extends EzyObjectSerializer {

	<T> T serialize(Object value, Class<T> outType);
	
	@Override
	default byte[] serialize(Object value) {
		return serialize(value, byte[].class);
	}
	
}
