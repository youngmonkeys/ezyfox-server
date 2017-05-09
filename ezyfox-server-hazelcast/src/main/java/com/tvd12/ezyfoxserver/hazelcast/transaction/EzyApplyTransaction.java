package com.tvd12.ezyfoxserver.hazelcast.transaction;

import com.tvd12.ezyfoxserver.function.EzyExceptionApply;

public interface EzyApplyTransaction<T> extends EzyTransaction {
	
	void apply(EzyExceptionApply<T> func) throws Exception;
	
}
