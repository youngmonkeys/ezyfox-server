package com.tvd12.ezyfoxserver.hazelcast.testing.mapstore;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.testing.HazelcastBaseTest;

public class ExampleMongoMapstoreTest extends HazelcastBaseTest {

	@Test
	public void test() {
		ExampleMongoMapstore mapstore = new ExampleMongoMapstore();
		mapstore.setMongoClient(MONGO_CLIENT);
	}
	
}
