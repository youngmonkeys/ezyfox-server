package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import com.tvd12.ezyfoxserver.hazelcast.EzyMongoDatastoreHazelcastFactory;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyMaxIdService;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleMaxIdService;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.test.base.BaseTest;

public abstract class HazelcastBaseTest extends BaseTest {

	protected static final MongoClient MONGO_CLIENT;
	protected static final EzyMaxIdService MAX_ID_SERVICE;
	protected static final HazelcastInstance HZ_INSTANCE;
	
	static {
		MONGO_CLIENT = newMongoClient();
		HZ_INSTANCE = newHzInstance();
		MAX_ID_SERVICE = newMaxIdService();
		
		Runtime.getRuntime().addShutdownHook(new Thread(()-> {
		    System.err.println("\n\nshutdown hook, close mongo client\n\n");
			MONGO_CLIENT.close();
		}));
	}
	
	private static MongoClient newMongoClient() {
		return new EzyInputStreamMongoClientLoader()
				.inputStream(getConfigStream())
				.load();
	}
	
	private static EzyMaxIdService newMaxIdService() {
		EzySimpleMaxIdService service = new EzySimpleMaxIdService(HZ_INSTANCE);
		return service;
	}
	
	private static HazelcastInstance newHzInstance() {
		EzyMongoDatastoreHazelcastFactory factory = new EzyMongoDatastoreHazelcastFactory();
		factory.setDatabase(MONGO_CLIENT.getDatabase("test"));
		return factory.newHazelcast(new Config());
	}
	
	private static InputStream getConfigStream() {
		return HazelcastBaseTest.class.getResourceAsStream("/mongo_config.properties");
	}
	
	protected Object newServiceBuilder() {
		return null;
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
