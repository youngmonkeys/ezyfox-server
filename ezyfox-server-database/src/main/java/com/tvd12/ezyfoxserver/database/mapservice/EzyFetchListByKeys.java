package com.tvd12.ezyfoxserver.database.mapservice;

import java.util.List;

public interface EzyFetchListByKeys<K,V> {

	List<V> getListByIds(Iterable<K> keys);
	
}
