package com.tvd12.ezyfoxserver.database.service;

import com.tvd12.ezyfoxserver.database.query.EzyFindAndModifyOptions;
import com.tvd12.ezyfoxserver.database.query.EzyUpdateOperations;
import com.tvd12.ezyfoxserver.function.EzyApply;

public interface EzyFindAndModifyByIdService<I,E> {
	
	E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations);
	
	E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations, EzyApply<EzyFindAndModifyOptions> options);
	
}
