package com.tvd12.ezyfoxserver.hazelcast.transaction;

import com.hazelcast.core.TransactionalMap;

public interface EzyMapReturnTransaction<K,V,R> 
		extends EzyReturnTransaction<TransactionalMap<K, V>, R> {

}
