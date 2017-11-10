package com.tvd12.ezyfoxserver.morphia.testing;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezyfoxserver.mongodb.loader.EzyPropertiesMongoClientLoader;
import com.tvd12.test.base.BaseTest;

public class EzyPropertiesMongoClientLoaderTest extends BaseTest {

	@Test
	public void test() {
		Properties properties = new Properties();
		properties.setProperty(EzyMongoClientLoader.HOST, "127.0.0.1");
		properties.setProperty(EzyMongoClientLoader.PORT, "27017");
		properties.setProperty(EzyMongoClientLoader.USERNAME, "root");
		properties.setProperty(EzyMongoClientLoader.PASSWORD, "123456");
		properties.setProperty(EzyMongoClientLoader.DATABASE, "test");
		EzyPropertiesMongoClientLoader.load(properties).close();
	}
	
}
