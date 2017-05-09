package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.ezyfoxserver.hazelcast.testing.mapstore.ExampleUserMapstore;
import com.tvd12.test.base.BaseTest;

public class EzySimpleMapstoresFetcherTest extends BaseTest {

	@Test
	public void test() {
		EzySimpleMapstoresFetcher fetcher = EzySimpleMapstoresFetcher.builder()
				.scan("com.tvd12.ezyfoxserver.hazelcast.testing.mapstore", "com.tvd12.ezyfoxserver.hazelcast.testing.mapstore")
				.addMapstoreClass(Class.class)
				.addMapstoreClass("hello", Class.class)
				.addMapstoreClass("example_users", ExampleUserMapstore.class)
				.addMapstore(new ExampleUserMapstore())
				.addMapstore("example_users", new ExampleUserMapstore())	
				.build();
		assert fetcher.getMapstores().size() == 1;
	}
	
}
