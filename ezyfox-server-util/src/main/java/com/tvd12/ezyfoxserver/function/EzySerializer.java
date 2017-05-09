package com.tvd12.ezyfoxserver.function;

public interface EzySerializer<I,O> {

	O serialize(I input);
	
}
