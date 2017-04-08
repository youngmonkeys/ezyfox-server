package com.tvd12.ezyfoxserver.function;

public interface EzySerializer<F,T> {

	T serialize(F value);
	
}
