package com.tvd12.ezyfoxserver.morphia.testing;

import java.io.InputStream;
import java.util.Map;

import org.mongodb.morphia.Datastore;

import com.mongodb.MongoClient;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.mongodb.bean.EzyRepositoriesImplementer;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezyfoxserver.morphia.EzyDataStoreBuilder;
import com.tvd12.ezyfoxserver.morphia.bean.EzyMorphiaRepositories;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.stream.EzyAnywayInputStreamLoader;
import com.tvd12.test.base.BaseTest;

public class BaseMongoDBTest extends BaseTest {

	protected static final MongoClient MONGO_CLIENT = newMongoClient();
	protected static final Datastore DATASTORE = newDataStore();
	protected static final EzyBeanContext BEAN_CONTEXT = newBeanContext();
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> MONGO_CLIENT.close()));
	}
	
	private static EzyBeanContext newBeanContext() {
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.addSingleton("datastore", DATASTORE)
				.scan("com.tvd12.ezyfoxserver.morphia.testing.repo")
				.scan("com.tvd12.ezyfoxserver.morphia.testing.service");
		EzyRepositoriesImplementer implementer = EzyMorphiaRepositories.newRepositoriesImplementer()
				.scan("com.tvd12.ezyfoxserver.morphia.testing.repo", "com.tvd12.ezyfoxserver.morphia.testing.repo1");
		Map<Class<?>, Object> repos = implementer.implement(DATASTORE);
		for(Class<?> key : repos.keySet()) {
			builder.addSingleton(EzyClasses.getVariableName(key), repos.get(key));
		}
		return builder.build();
	}
			
	private static Datastore newDataStore() {
		return EzyDataStoreBuilder.dataStoreBuilder()
				.mongoClient(MONGO_CLIENT)
				.databaseName("test")
				.scan("com.tvd12.ezyfoxserver.morphia.testing.data")
				.addEntityClasses(Pig.class, Duck.class)
				.build();
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
				.context(BaseMongoDBTest.class)
				.build()
				.load("mongodb_config.properties");
	}
	
}
