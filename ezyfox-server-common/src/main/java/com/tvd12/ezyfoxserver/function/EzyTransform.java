package com.tvd12.ezyfoxserver.function;

public interface EzyTransform<I, O> {

	O transform(I input);
	
}
