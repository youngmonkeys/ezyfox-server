package com.tvd12.ezyfoxserver.hazelcast.testing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzySimpleMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyMaxIdService;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleMaxIdService;
import com.tvd12.test.base.BaseTest;

public abstract class HazelcastBaseTest extends BaseTest {

	protected static final HazelcastInstance HZ_INSTANCE;
	protected static final EzyMaxIdService MAX_ID_SERVICE;
	protected static final EzyMapTransactionFactory MAP_TRANSACTION_FACTORY;
	
	static {
		HZ_INSTANCE = newHzInstance();
		MAP_TRANSACTION_FACTORY = newMapTransactionFactory();
		MAX_ID_SERVICE = newMaxIdService();
	}
	
	private static HazelcastInstance newHzInstance() {
		return new ExampleHazelcastCreator()
				.filePath("hazelcast.xml")
				.create();
	}
	
	private static EzyMaxIdService newMaxIdService() {
		EzySimpleMaxIdService service = new EzySimpleMaxIdService(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		return service;
	}
	
	private static EzyMapTransactionFactory newMapTransactionFactory() {
		return new EzySimpleMapTransactionFactory(HZ_INSTANCE);
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
