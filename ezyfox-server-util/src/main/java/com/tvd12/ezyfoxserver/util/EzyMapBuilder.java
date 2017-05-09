package com.tvd12.ezyfoxserver.util;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzyMapBuilder implements EzyBuilder<Map> {

	protected Map map;
	protected Map container = new HashMap<>();
	
	public static EzyMapBuilder mapBuilder() {
		return new EzyMapBuilder();
	}
	
	public EzyMapBuilder map(Map map) {
		this.map = map;
		return this;
	}
	
	public EzyMapBuilder putAll(Map map) {
		this.container.putAll(map);
		return this;
	}
	
	public EzyMapBuilder put(Object key, Object value) {
		this.container.put(key, value);
		return this;
	}
	
	@Override
	public Map build() {
		if(map == null) return container;
		map.putAll(container);
		return map;
	}
}
