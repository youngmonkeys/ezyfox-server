package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyRemoveByKeysService<K> {

	void remove(Iterable<K> keys);
	
}
