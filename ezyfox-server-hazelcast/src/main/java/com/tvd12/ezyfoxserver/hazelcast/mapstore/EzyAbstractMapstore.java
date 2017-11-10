package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoaderLifecycleSupport;
import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyPostInit;

public abstract class EzyAbstractMapstore<K, V> 
		extends EzyLoggable
		implements MapStore<K, V>, MapLoaderLifecycleSupport, EzyPostInit {

	protected Properties properties;
	protected HazelcastInstance hzInstance;

	@Override
	public final void init(
			HazelcastInstance hzInstance, Properties properties, String mapName) {
		initComponents(hzInstance, properties, mapName);
		config(hzInstance, properties, mapName);
	}

	private final void initComponents(
			HazelcastInstance hzInstance, Properties properties, String mapName) {
		this.properties = properties;
		this.hzInstance = hzInstance;
	}

	protected void config(
			HazelcastInstance hzInstance, Properties properties, String mapName) {
	}

	@Override
	public void postInit() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void delete(K key) {
	}

	@Override
	public Map<K, V> loadAll(Collection<K> keys) {
		Map<K, V> map = new HashMap<>();
		for (K key : keys)
			map.put(key, load(key));
		return map;
	}

	@Override
	public Iterable<K> loadAllKeys() {
		return new HashSet<>();
	}

	@Override
	public void storeAll(Map<K, V> map) {
		for (K key : map.keySet())
			store(key, map.get(key));
	}

	@Override
	public void deleteAll(Collection<K> keys) {
	}

	@SuppressWarnings("unchecked")
	protected final <T> T getProperty(Object key) {
		return (T) properties.get(key);
	}

	protected final boolean containsProperty(Object key) {
		return properties.containsKey(key);
	}

}
