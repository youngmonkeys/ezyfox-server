package com.tvd12.ezyfoxserver.hazelcast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Sets;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.tvd12.ezyfoxserver.util.EzyHasIdEntity;

public abstract class EzySimpleHazelcastMapService<K,V>
		extends EzyAbstractMapService<K, V>
		implements EzyHazelcastMapService<K, V> {
	
	public EzySimpleHazelcastMapService() {
	}

	public EzySimpleHazelcastMapService(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}
	
	@Override
	public V get(K key) {
		return map.get(key);
	}
	
	@Override
	public List<V> getAllList() {
		return new ArrayList<>(map.values());
	}
	
	@Override
	public Map<K, V> getAllMap() {
		return new HashMap<>(map);
	}
	
	@Override
	public List<V> getListByIds(Iterable<K> keys) {
		return new ArrayList<>(getMapByIds(keys).values());
	}
	
	@Override
	public Map<K, V> getMapByIds(Iterable<K> keys) {
		return map.getAll(Sets.newHashSet(keys));
	}
	
	@Override
	public int size() {
		return map.size();
	}
	
	@Override
	public void set(K key, V value) {
		map.set(key, value);
	}
	
	@Override
	public void set(V value) {
		map.set(getKey(value), value);
	}
	
	@Override
	public void set(Map<K, V> map) {
		put(map);
	}
	
	@Override
	public void set(Iterable<V> values) {
		put(values);
	}
	
	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}
	
	@Override
	public V put(V value) {
		return map.put(getKey(value), value);
	}
	
	@Override
	public void put(Map<K, V> map) {
		this.map.putAll(map);
	}
	
	@Override
	public void put(Iterable<V> values) {
		Map<K, V> map = new HashMap<>();
		values.forEach(v -> map.put(getKey(v), v));
		put(map);
	}
	
	@SuppressWarnings("unchecked")
	private K getKey(V value) {
		if(value instanceof EzyHasIdEntity)
			return ((EzyHasIdEntity<K>)value).getId();
		throw new IllegalArgumentException("value must implements 'EzyHasIdEntity' interface");
	}
	
	@Override
	public V remove(K key) {
		return map.remove(key);
	}
	
	@Override
	public void remove(Iterable<K> keys) {
		keys.forEach(this::remove);
	}
	
	@Override
	public void clear() {
		map.clear();
	}
	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	@Override
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
	@Override
	public boolean containsValue(V value) {
		return map.containsValue(value);
	}
	
	@SuppressWarnings("rawtypes")
	public List<V> getListByField(String field, Comparable value) {
		EntryObject e = getEntryObject();
		Predicate predicate = e.get(field).equal(value);
		return getList(predicate);
	}
	
	@SuppressWarnings("rawtypes")
	protected List<V> getList(Predicate predicate) {
		return new ArrayList<>(map.values(predicate));
	}
	
	protected final EntryObject getEntryObject() {
		return new PredicateBuilder().getEntryObject();
	}
	
}
