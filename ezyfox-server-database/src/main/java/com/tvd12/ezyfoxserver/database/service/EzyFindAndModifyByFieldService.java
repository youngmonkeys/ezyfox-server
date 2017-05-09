package com.tvd12.ezyfoxserver.database.service;

import com.tvd12.ezyfoxserver.database.query.EzyFindAndModifyOptions;
import com.tvd12.ezyfoxserver.database.query.EzyUpdateOperations;
import com.tvd12.ezyfoxserver.function.EzyApply;

public interface EzyFindAndModifyByFieldService<E> {
	
	E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations);
	
	E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations, EzyApply<EzyFindAndModifyOptions> options);
	
}
