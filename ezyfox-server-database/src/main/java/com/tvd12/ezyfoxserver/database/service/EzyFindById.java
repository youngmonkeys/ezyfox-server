package com.tvd12.ezyfoxserver.database.service;

public interface EzyFindById<I,E> {

	E findById(I id);
	
}
