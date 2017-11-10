package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyFetchByKeyService<K,V> {

	V get(K key);
	
}
