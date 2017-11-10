package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.Properties;
import java.util.Set;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.hazelcast.config.Config;
import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.hazelcast.EzyAbstractHazelcastFactory;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyAbstractMapstore;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.testing.mapstore.ExampleUserMapstore;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractHazelcastFactoryTest extends BaseTest {

	@Test
	public void test() {
		EzyAbstractHazelcastFactory factory = new EzyAbstractHazelcastFactory() {
			@Override
			protected EzyMapstoreCreator newMapstoreCreator() {
				return new EzyMapstoreCreator() {
					
					@Override
					public Set<String> getMapNames() {
						return Sets.newHashSet("example_users", EzyMapNames.MAX_ID);
					}
					
					@SuppressWarnings("rawtypes")
					@Override
					public MapStore create(String mapName, Properties properties) {
						if(mapName.equals("example_users"))
							return new ExampleUserMapstore();
						return new EzyAbstractMapstore() {

							@Override
							public void store(Object key, Object value) {
							}

							@Override
							public Object load(Object key) {
								return null;
							}
							
						};
					}
				};
			}
		};
		factory.newHazelcast(new Config());
	}
	
}
