package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyFetchService<K,V> extends
		EzyFetchByKeyService<K, V>,
		EzyFetchAllService<K, V>,
		EzyFetchByKeys<K, V> {

}
