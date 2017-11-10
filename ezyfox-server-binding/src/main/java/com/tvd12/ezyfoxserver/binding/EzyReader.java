package com.tvd12.ezyfoxserver.binding;

public interface EzyReader<T,R> {
	
	R read(EzyUnmarshaller unmarshaller, T value);
	
}
