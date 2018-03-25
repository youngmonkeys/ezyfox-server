package com.tvd12.ezyfoxserver.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;

import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyHashMap implements EzyObject {
	private static final long serialVersionUID = 2273868568933801751L;
	
	protected HashMap<Object, Object> map = new HashMap<>();
	
	@Setter
	protected transient EzyInputTransformer inputTransformer;
	@Setter
	protected transient EzyOutputTransformer outputTransformer;
	
	public EzyHashMap() {
	}
	
	@SuppressWarnings("rawtypes")
	public EzyHashMap(Map map) {
		this.map.putAll(map);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyObject#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public <V> V put(Object key, Object value) {
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
	public <V> V get(Object key, Class<V> type) {
		return (V) getValue(key, type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#getValue(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getValue(Object key, Class type) {
		return transformOutput(map.get(key), type);
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
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoObject#isNotNullKey(java.lang.Object)
	 */
	@Override
	public boolean isNotNullValue(Object key) {
		return map.get(key) != null;
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
		return new HashMap<>(map);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		EzyHashMap clone = new EzyHashMap(map);
		clone.setInputTransformer(inputTransformer);
		clone.setOutputTransformer(outputTransformer);
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
	
	private Object transformInput(Object input) {
		return inputTransformer.transform(input);
	}
	
	@SuppressWarnings("rawtypes")
	private Object transformOutput(Object output, Class type) {
		return outputTransformer.transform(output, type);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
