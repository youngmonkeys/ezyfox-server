package com.tvd12.ezyfoxserver.hazelcast.transaction;

import com.tvd12.ezyfoxserver.function.EzyExceptionFunction;

public interface EzyReturnTransaction<T,R> extends EzyTransaction {
	
	R apply(EzyExceptionFunction<T, R> func) throws Exception;
	
}
