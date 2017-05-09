package com.tvd12.ezyfoxserver.function;

public interface EzyDeserializer<I,O> {

	O deserialize(I input);
	
}
