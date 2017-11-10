package com.tvd12.ezyfoxserver.database.service;

import com.tvd12.ezyfoxserver.database.query.EzyUpdateOperations;
import com.tvd12.ezyfoxserver.function.EzyApply;

public interface EzyUpdateOneByFieldService<E> {

	void updateOneByField(String field, Object value, E entity);
	
	void updateOneByField(String field, Object value, E entity, boolean upsert);
	
	void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations);
	
	void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert);
	
}
