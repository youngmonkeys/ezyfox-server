package com.tvd12.ezyfoxserver.hazelcast.transaction.impl;

import com.hazelcast.core.TransactionalMap;
import com.hazelcast.transaction.TransactionContext;
import com.tvd12.ezyfoxserver.function.EzyExceptionFunction;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapReturnTransaction;

public class EzySimpleMapReturnTransaction<K,V,R>
		extends EzySimpleTransaction
		implements EzyMapReturnTransaction<K, V, R> {

	protected final String mapName;
	
	public EzySimpleMapReturnTransaction(
			TransactionContext context, String mapName) {
		super(context);
		this.mapName = mapName;
	}
	
	@Override
	public R apply(EzyExceptionFunction<TransactionalMap<K, V>, R> func) throws Exception {
		TransactionalMap<K, V> map = context.getMap(mapName);
		return func.apply(map);
	}
	
}
