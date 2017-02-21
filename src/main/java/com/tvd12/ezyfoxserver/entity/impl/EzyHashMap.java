package com.tvd12.ezyfoxserver.entity.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyHashMap extends HashMap<Object, Object> implements EzyObject {
	private static final long serialVersionUID = 2273868568933801751L;
	
	@Setter
	private EzyInputTransformer inputTransformer;
	@Setter
	private EzyOutputTransformer outputTransformer;

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
		return (V) transformOutput(super.get(key), clazz);
	}
	
	@Override
	public Map<? extends Object, ? extends Object> toMap() {
		return this;
	}
	
	private Object transformInput(final Object input) {
		return inputTransformer.transform(input);
	}
	
	@SuppressWarnings("rawtypes")
	private Object transformOutput(final Object output, final Class type) {
		return outputTransformer.transform(output, type);
	}
}
