package com.tvd12.ezyfoxserver.entity.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.Set;

import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyHashMap implements EzyObject {
	private static final long serialVersionUID = 2273868568933801751L;
	
	private HashMap<Object, Object> map = new HashMap<>();
	
	@Setter
	private transient EzyInputTransformer inputTransformer;
	@Setter
	private transient EzyOutputTransformer outputTransformer;
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public <V> V put(Object key, Object value) {
		if(key == null)
			throw new IllegalArgumentException("key can't be null");
		return (V) map.put(key, transformInput(value));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#putAll(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void putAll(Map m) {
		for(Object key : m.keySet())
			put(key, m.get(key));
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#get(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <V> V get(Object key, Class<V> clazz) {
		return (V) transformOutput(map.get(key), clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#remove(java.lang.Object)
	 */
	@Override
	public <V> V remove(Object key) {
		return (V) map.remove(key);
	}
	
	/*
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <V> V compute(Object key, BiFunction func) {
		return (V) map.compute(key, func);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#get(java.lang.Object)
	 */
	@Override
	public <V> V get(Object key) {
		return (V) map.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#keySet()
	 */
	@Override
	public Set<Object> keySet() {
		return map.keySet();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#entrySet()
	 */
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#clear()
	 */
	@Override
	public void clear() {
		map.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#toMap()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map toMap() {
		return map;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		EzyHashMap clone = new EzyHashMap();
		clone.setInputTransformer(inputTransformer);
		clone.setOutputTransformer(outputTransformer);
		clone.map.putAll(map);
		return clone;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#duplicate()
	 */
	@Override
	public EzyObject duplicate() {
		try {
			return (EzyObject) clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private Object transformInput(final Object input) {
		return inputTransformer.transform(input);
	}
	
	@SuppressWarnings("rawtypes")
	private Object transformOutput(final Object output, final Class type) {
		return outputTransformer.transform(output, type);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
