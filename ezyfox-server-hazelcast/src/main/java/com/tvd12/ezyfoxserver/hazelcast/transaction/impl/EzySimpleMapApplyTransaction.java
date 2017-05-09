package com.tvd12.ezyfoxserver.hazelcast.transaction.impl;

import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;
import com.tvd12.ezyfoxserver.function.EzyExceptionApply;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapApplyTransaction;

public class EzySimpleMapApplyTransaction<K,V,R>
		extends EzySimpleTransaction
		implements EzyMapApplyTransaction<K, V> {

	protected final String mapName;
	
	public EzySimpleMapApplyTransaction(
			TransactionContext context, String mapName) {
		super(context);
		this.mapName = mapName;
	}
	
	@Override
	public void apply(EzyExceptionApply<TransactionalMap<K, V>> func) throws Exception {
		TransactionalMap<K, V> map = context.getMap(mapName);
		func.apply(map);
	}
	
}
