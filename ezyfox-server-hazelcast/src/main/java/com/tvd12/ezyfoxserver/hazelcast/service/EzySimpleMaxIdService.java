/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.TransactionalMap;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;
import com.tvd12.ezyfoxserver.hazelcast.transaction.EzyMapReturnTransaction;

/**
 * @author tavandung12
 *
 */
public class EzySimpleMaxIdService
		extends EzyAbstractMapService<String, Long>
		implements EzyMaxIdService {
	
	public EzySimpleMaxIdService() {
	}

	public EzySimpleMaxIdService(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}
	
	@Override
	public void loadAll() {
		map.loadAll(false);
	}

	@Override
	public Long incrementAndGet(String key) {
		EzyMapReturnTransaction<String, Long, Long> transaction = 
				newReturnTransaction();
		transaction.begin();
		try {
			Long maxId = 
			transaction.apply(map -> incrementAndGetMaxId(map, key));
			transaction.commit();
			return maxId;
		}
		catch(Exception e) {
			transaction.rollback();
			throw new IllegalStateException("cannot increment and get on key " + key, e);
		}
	}
	
	private Long incrementAndGetMaxId(TransactionalMap<String, Long> map, String key) {
		Long current = map.getForUpdate(key);
		Long maxId = current != null ? current + 1L : 1L;
		map.set(key, maxId);
		return maxId;
	}

	@Override
	protected String getMapName() {
		return EzyMapNames.MAX_ID;
	}

}
