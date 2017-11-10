package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.Test;

import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyAbstractMapstoreFactory;

public class EzyAbstractMapstoreFactoryTest {

	@Test
	public void test() {
		EzyAbstractMapstoreFactory factory = new EzyAbstractMapstoreFactory() {
			
			@Override
			protected Object newMapstore(String mapName, Properties properties) {
				return new MapStore<String, String>() {

					@Override
					public String load(String key) {
						return null;
					}

					@Override
					public Map<String, String> loadAll(Collection<String> keys) {
						return null;
					}

					@Override
					public Iterable<String> loadAllKeys() {
						return null;
					}

					@Override
					public void store(String key, String value) {
					}

					@Override
					public void storeAll(Map<String, String> map) {
					}

					@Override
					public void delete(String key) {
					}

					@Override
					public void deleteAll(Collection<String> keys) {
					}
					
				};
			}
		};
		factory.newMapStore("map", new Properties());
	}
	
}
