package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyPutKeyValueService<K,V> {

	V put(K key, V value);
	
}
