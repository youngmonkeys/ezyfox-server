package com.tvd12.ezyfoxserver.function;

public interface EzyTransformer<I, O> {

	O transform(I input);
	
}
