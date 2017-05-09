package com.tvd12.ezyfoxserver.hazelcast.testing.mapstore;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMongoMaxIdMapstore;
import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;

public class EzyMongoMaxIdMapstoreTest extends HazelcastBaseTest {

	@Test
	public void test() {
		EzyMongoMaxIdMapstore mapstore = new EzyMongoMaxIdMapstore();
		mapstore.setDatabase(DATABASE);
		mapstore.postInit();
		mapstore.load("not exists key");
	}
	
}
