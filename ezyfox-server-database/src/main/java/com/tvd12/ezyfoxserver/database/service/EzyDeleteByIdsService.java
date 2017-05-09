package com.tvd12.ezyfoxserver.database.service;

import java.util.Collection;

public interface EzyDeleteByIdsService<I> {

	int deleteByIds(Collection<I> ids);
	
}
