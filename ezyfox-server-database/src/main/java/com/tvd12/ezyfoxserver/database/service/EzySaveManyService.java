package com.tvd12.ezyfoxserver.database.service;

import com.google.common.collect.Lists;

public interface EzySaveManyService<E> {

	void save(Iterable<E> entities);
	
	@SuppressWarnings("unchecked")
	default void save(E... entities) {
		save(Lists.newArrayList(entities));
	}
	
}
