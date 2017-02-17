package com.tvd12.ezyfoxserver.function;

public interface EzyFoxTransformer<I, O> {

	O transform(I input);
	
}
