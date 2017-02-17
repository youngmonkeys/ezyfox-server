package com.tvd12.ezyfoxserver.entity.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.entity.EzyFoxObject;
import com.tvd12.ezyfoxserver.transformer.EzyFoxInputTransformer;

@SuppressWarnings("unchecked")
public class EzyFoxHashMap extends HashMap<Object, Object> implements EzyFoxObject {
	private static final long serialVersionUID = 2273868568933801751L;
	
	private EzyFoxInputTransformer inputTransformer;

	@Override
	public Object put(Object key, Object value) {
		if(key == null)
			throw new IllegalArgumentException("key can't be null");
		if(value == null)
			return super.put(key, value);
		return super.put(key, transformInput(value));
	}

	@Override
	public <V> V get(Object key, Class<V> clazz) {
		return null;
	}
	
	@Override
	public Map<? extends Object, ? extends Object> toMap() {
		return this;
	}
	
	private Object transformInput(Object input) {
		return inputTransformer.transform(input);
	}
}
