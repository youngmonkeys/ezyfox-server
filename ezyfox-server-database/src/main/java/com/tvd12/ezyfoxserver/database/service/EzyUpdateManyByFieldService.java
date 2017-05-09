package com.tvd12.ezyfoxserver.database.service;

import com.tvd12.ezyfoxserver.database.query.EzyUpdateOperations;
import com.tvd12.ezyfoxserver.function.EzyApply;

public interface EzyUpdateManyByFieldService<E> {

	void updateManyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations);
	
}
