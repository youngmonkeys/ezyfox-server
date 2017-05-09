package com.tvd12.ezyfoxserver.hazelcast.testing.mapstore;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoresFetcher;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.test.base.BaseTest;

public class EzySimpleMapstoreCreatorTest extends BaseTest {

	@Test
	public void test() {
		EzyMapstoresFetcher fetcher = newMapstoresFetcher();
		EzySimpleMapstoreCreator creator = new EzySimpleMapstoreCreator();
		creator.setMapstoresFetcher(fetcher);
		try {
			creator.create("no", new Properties());
		}
		catch(Exception e) {
		}
	}
	
	private EzyMapstoresFetcher newMapstoresFetcher() {
		return EzySimpleMapstoresFetcher.builder()
				.scan("com.tvd12.ezyfoxserver.hazelcast.testing.mapstore")
				.build();
	}
	
}
