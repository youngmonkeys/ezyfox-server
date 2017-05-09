package com.tvd12.ezyfoxserver.binding;

public interface EzyWriter<T,R> {
	
	R write(EzyMarshaller marshaller, T object);
	
}
