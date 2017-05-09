package com.tvd12.ezyfoxserver.hazelcast.testing;


import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzySimpleMapTransactionFactory;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyMaxIdService;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleMaxIdService;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezyfoxserver.stream.EzyAnywayInputStreamLoader;
import com.tvd12.test.base.BaseTest;

public abstract class HazelcastBaseTest extends BaseTest {

	protected static final MongoClient MONGO_CLIENT;
	protected static final MongoDatabase DATABASE;
	protected static final HazelcastInstance HZ_INSTANCE;
	protected static final EzyMaxIdService MAX_ID_SERVICE;
	protected static final EzyMapTransactionFactory MAP_TRANSACTION_FACTORY;
	
	static {
		MONGO_CLIENT = newMongoClient();
		DATABASE = MONGO_CLIENT.getDatabase("test");
		HZ_INSTANCE = newHzInstance();
		MAP_TRANSACTION_FACTORY = newMapTransactionFactory();
		MAX_ID_SERVICE = newMaxIdService();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> MONGO_CLIENT.close()));
	}
	
	private static HazelcastInstance newHzInstance() {
		return new ExampleHazelcastCreator()
				.filePath("hazelcast.xml")
				.database(MONGO_CLIENT.getDatabase("test"))
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
	
	private static MongoClient newMongoClient() {
		return new EzyInputStreamMongoClientLoader()
				.inputStream(getMongoConfigInputStream())
				.property(EzyMongoClientLoader.DATABASE, "test")
				.properties(EzyMaps.newHashMap(EzyMongoClientLoader.DATABASE, "test"))
				.load();
	}
	
	private static InputStream getMongoConfigInputStream() {
		return EzyAnywayInputStreamLoader.builder()
				.context(HazelcastBaseTest.class)
				.build()
				.load("mongo_config.properties");
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
