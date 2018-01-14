package com.tvd12.ezyfoxserver.util;

import java.util.Collection;
import java.util.HashMap;

public abstract class EzyHashMapCollection<K,E,V extends Collection<E>> 
		extends HashMap<K, V>
		implements EzyMapCollection<K, E, V> {
	private static final long serialVersionUID = 3535251482476794711L;

	@Override
	public void addItems(K key, Collection<E> items) {
		computeIfAbsent(key, k -> newCollection()).addAll(items);
	}
	
	public V getItems(K key) {
		return containsKey(key) ? get(key) : newCollection();
	}

	protected abstract V newCollection();
	
	@Override
	public void deepClear() {
		for(V value : values())
			value.clear();
		this.clear();
	}
	
}
