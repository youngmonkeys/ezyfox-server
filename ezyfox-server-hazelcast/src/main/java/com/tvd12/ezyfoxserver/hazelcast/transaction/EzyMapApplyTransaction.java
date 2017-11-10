package com.tvd12.ezyfoxserver.hazelcast.transaction;

import com.hazelcast.core.TransactionalMap;

public interface EzyMapApplyTransaction<K,V> 
		extends EzyApplyTransaction<TransactionalMap<K, V>> {

}
