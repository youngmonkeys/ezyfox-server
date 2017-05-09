package com.tvd12.ezyfoxserver.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.TransactionalMap;
import com.tvd12.ezyfoxserver.function.EzyExceptionApply;
import com.tvd12.ezyfoxserver.function.EzyExceptionFunction;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyMapTransactionFactoryAware;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapReturnTransaction;

import lombok.Setter;

public abstract class EzyAbstractMapService<K,V>
		extends EzyAbstractHazelcastService
		implements EzyMapTransactionFactoryAware {

	protected IMap<K, V> map;

	@Setter
	protected EzyMapTransactionFactory mapTransactionFactory;
	
	public EzyAbstractMapService() {
	}
	
	public EzyAbstractMapService(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}
	
	@Override
	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		super.setHazelcastInstance(hazelcastInstance);
		this.map = hazelcastInstance.getMap(getMapName());
	}
	
	protected abstract String getMapName();
	
	protected EzyMapApplyTransaction<K, V> newApplyTransaction() {
		return mapTransactionFactory.newApplyTransaction(getMapName());
	}
	
	protected <R> EzyMapReturnTransaction<K, V, R> newReturnTransaction() {
		return mapTransactionFactory.newReturnTransaction(getMapName());
	}
	
	protected final void transactionUpdate(
			EzyExceptionApply<TransactionalMap<K, V>> applier) throws Exception {
		EzyMapApplyTransaction<K, V> transaction = newApplyTransaction();
		transaction.begin();
		try {
			transaction.apply(applier);
			transaction.commit();
		}
		catch(Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	protected final void transactionUpdate(
			K key, EzyExceptionApply<V> applier, V defValue) throws Exception {
		transactionUpdate(map -> transactionUpdate(map, key, applier, defValue));
	}
	
	protected final <R> R transactionUpdateAndGet(
			EzyExceptionFunction<TransactionalMap<K, V>, R> applier) throws Exception {
		EzyMapReturnTransaction<K, V, R> transaction = newReturnTransaction();
		transaction.begin();
		try {
			R answer = 
			transaction.apply(applier);
			transaction.commit();
			return answer;
		}
		catch(Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	protected final <R> R transactionUpdateAndGet(
			K key, EzyExceptionFunction<V, R> applier, V defValue) throws Exception {
		return transactionUpdateAndGet(map -> transactionUpdateAndGet(map, key, applier, defValue));
	}
	
	private void transactionUpdate(
			TransactionalMap<K, V> map, K key, EzyExceptionApply<V> applier, V defValue) throws Exception {
		V value = map.getForUpdate(key);
    	if(value == null)
    		value = defValue;
    	if(value != null) {
    		applier.apply(value);
    		map.set(key, value);
    	}
	}
	
	private <R> R transactionUpdateAndGet(
			TransactionalMap<K, V> map, K key, EzyExceptionFunction<V, R> applier, V defValue) throws Exception {
		V value = map.getForUpdate(key);
    	if(value == null)
    		value = defValue;
    	R result = null;
    	if(value != null) {
    		result = applier.apply(value);
    		map.set(key, value);
    	}
    	return result;
	}
	
}
