package com.tvd12.ezyfoxserver.function;

public interface EzyExceptionFunction<T,R> {

	R apply(T t) throws Exception;
	
}
