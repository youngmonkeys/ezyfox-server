package com.tvd12.ezyfoxserver.database.service;

public interface EzyFindByField<E> {

	E findByField(String field, Object value);
	
}
