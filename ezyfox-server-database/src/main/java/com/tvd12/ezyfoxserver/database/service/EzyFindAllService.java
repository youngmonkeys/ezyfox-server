package com.tvd12.ezyfoxserver.database.service;

import java.util.List;

public interface EzyFindAllService<E> {

	List<E> findAll();
	
	List<E> findAll(int skip, int limit);
}
