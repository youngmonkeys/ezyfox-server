package com.tvd12.ezyfoxserver.function;

public interface EzyDeserializer<F,T> {

	T deserialize(F value);
	
}
