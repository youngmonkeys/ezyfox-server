package com.tvd12.ezyfoxserver.reflect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings("rawtypes")
public final class EzyTypes {

	public static final Set<Class> PRIMITIVE_TYPES = primitiveTypes();
	public static final Set<Class> WRAPPER_TYPES = wrapperTypes();
	public static final Set<Class> ARRAY_PRIMITIVE_TYPES = arrayPrimitiveTypes();
	public static final Set<Class> ARRAY_WRAPPER_TYPES = arrayWrapperTypes();
	public static final Set<Class> TWO_DIMENSIONS_ARRAY_PRIMITIVE_TYPES = twoDimensionsArrayPrimitiveTypes();
	public static final Set<Class> TWO_DIMENSIONS_ARRAY_WRAPPER_TYPES = twoDimensionsArrayWrapperTypes();
	public static final Set<Class> STRING_TYPES = stringTypes();
	public static final Set<Class> ALL_TYPES = mergeAllTypes();
	public static final Set<Class> COMMON_GENERIC_TYPES = commonGenericTypes();
	public static final Map<Class, Class> PRIMITIVE_WRAPPER_TYPES_MAP = mapPrimitiveAndWrapperTypes();
	
	private EzyTypes() {
	}
	
	private static final Set<Class> primitiveTypes() {
		Set<Class> set = Sets.newHashSet(
				boolean.class, 
				byte.class, 
				char.class, 
				double.class, 
				float.class, 
				int.class, 
				long.class, 
				short.class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> wrapperTypes() {
		Set<Class> set = Sets.newHashSet(
				Boolean.class, 
				Byte.class, 
				Character.class, 
				Double.class, 
				Float.class, 
				Integer.class, 
				Long.class, 
				Short.class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> arrayPrimitiveTypes() {
		Set<Class> set = Sets.newHashSet(
				boolean[].class, 
				byte[].class, 
				char[].class, 
				double[].class, 
				float[].class, 
				int[].class, 
				long[].class, 
				short[].class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> arrayWrapperTypes() {
		Set<Class> set = Sets.newHashSet(
				Boolean[].class, 
				Byte[].class, 
				Character[].class, 
				Double[].class, 
				Float[].class, 
				Integer[].class, 
				Long[].class, 
				Short[].class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> twoDimensionsArrayPrimitiveTypes() {
		Set<Class> set = Sets.newHashSet(
				boolean[][].class, 
				byte[][].class, 
				char[][].class, 
				double[][].class, 
				float[][].class, 
				int[][].class, 
				long[][].class, 
				short[][].class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> twoDimensionsArrayWrapperTypes() {
		Set<Class> set = Sets.newHashSet(
				Boolean[][].class, 
				Byte[][].class, 
				Character[][].class, 
				Double[][].class, 
				Float[][].class, 
				Integer[][].class, 
				Long[][].class,
				Short[][].class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> stringTypes() {
		Set<Class> set = Sets.newHashSet(
				String.class, 
				String[].class, 
				String[][].class
		);
		return Collections.unmodifiableSet(set);
	}
	
	private static final Set<Class> mergeAllTypes() {
		Set<Class> merge = new HashSet<>();
		merge.addAll(Lists.newArrayList(PRIMITIVE_TYPES));
		merge.addAll(Lists.newArrayList(WRAPPER_TYPES));
		merge.addAll(Lists.newArrayList(ARRAY_PRIMITIVE_TYPES));
		merge.addAll(Lists.newArrayList(ARRAY_WRAPPER_TYPES));
		merge.addAll(Lists.newArrayList(TWO_DIMENSIONS_ARRAY_PRIMITIVE_TYPES));
		merge.addAll(Lists.newArrayList(TWO_DIMENSIONS_ARRAY_WRAPPER_TYPES));
		merge.addAll(Lists.newArrayList(STRING_TYPES));
		return Collections.unmodifiableSet(merge);
	}
	
	private static final Map<Class, Class> mapPrimitiveAndWrapperTypes() {
		Map<Class, Class> map = new ConcurrentHashMap<>();
		map.put(boolean.class, Boolean.class);
		map.put(byte.class, Byte.class);
		map.put(char.class, Character.class);
		map.put(double.class, Double.class);
		map.put(float.class, Float.class);
		map.put(int.class, Integer.class);
		map.put(long.class, Long.class);
		map.put(short.class, Short.class);
		return Collections.unmodifiableMap(map);
	}
	
	private static final Set<Class> commonGenericTypes() {
		Set<Class> set = Sets.newHashSet(
				Collection.class,
				List.class,
				ArrayList.class,
				CopyOnWriteArrayList.class,
				LinkedList.class,
				Set.class,
				HashSet.class,
				CopyOnWriteArraySet.class,
				Vector.class,
				Stack.class,
				Queue.class,
				Map.class,
				HashMap.class,
				TreeMap.class,
				ConcurrentHashMap.class
			);
		return Collections.unmodifiableSet(set);
	}
	
}
