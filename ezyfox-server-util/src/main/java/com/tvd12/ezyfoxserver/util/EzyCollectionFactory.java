package com.tvd12.ezyfoxserver.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

public class EzyCollectionFactory {

	@SuppressWarnings("rawtypes")
	protected final Map<Class, Supplier<Collection>> suppliers = defaultSuppliers();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Collection> T newCollection(Class collectionType) {
		if(suppliers.containsKey(collectionType))
			return (T) suppliers.get(collectionType).get();
		throw new IllegalArgumentException("unknown implementation of " + collectionType);
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, Supplier<Collection>> defaultSuppliers() {
		Map<Class, Supplier<Collection>> map = new ConcurrentHashMap<>();
		map.put(Collection.class, () -> new ArrayList<>());
		map.put(List.class, () -> new ArrayList<>());
		map.put(ArrayList.class, () -> new ArrayList<>());
		map.put(LinkedList.class, () -> new LinkedList<>());
		map.put(CopyOnWriteArrayList.class, () -> new CopyOnWriteArrayList<>());
		map.put(Set.class, () -> new HashSet<>());
		map.put(HashSet.class, () -> new HashSet<>());
		map.put(LinkedHashSet.class, () -> new LinkedHashSet<>());
		map.put(CopyOnWriteArraySet.class, () -> new CopyOnWriteArraySet<>());
		map.put(Vector.class, () -> new Vector<>());
		map.put(Queue.class, () -> new LinkedList<>());
		map.put(Stack.class, () -> new Stack<>());
		return map;
	}
	
}
