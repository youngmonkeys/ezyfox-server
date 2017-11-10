package com.tvd12.ezyfoxserver.database.service;

import java.util.Collection;
import java.util.List;

public interface EzyFindListByIds<I,E> {

	List<E> findListByIds(Collection<I> ids);
	
}
