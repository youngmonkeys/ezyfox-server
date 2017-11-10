package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyAbstractMapstore;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfoxserver.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfoxserver.hazelcast.testing.mapstore.ExampleUserMapstore;
import com.tvd12.ezyfoxserver.io.EzyMaps;

public class ExampleUserMapstoreTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		ExampleUserMapstore mapstore = new ExampleUserMapstore();
		Properties properties = new Properties();
		properties.put("hello", "world");
		mapstore.init(HZ_INSTANCE, properties, "example_users");
		mapstore.storeAll(EzyMaps.newHashMap("dungtv1", new ExampleUser("dungtv1")));
		mapstore.delete("dungtv");
		mapstore.destroy();
		
		Method getProperty = EzyAbstractMapstore.class.getDeclaredMethod("getProperty", Object.class);
		getProperty.setAccessible(true);
		Method containsProperty = EzyAbstractMapstore.class.getDeclaredMethod("containsProperty", Object.class);
		containsProperty.setAccessible(true);
		
		assert getProperty.invoke(mapstore, "hello") != null;
		assert containsProperty.invoke(mapstore, "hello").equals(Boolean.TRUE);
	}
	
}
