package com.tvd12.ezyfoxserver.bean.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.bean.EzyBeanNameTranslator;
import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanKey.*;

public class EzySimpleBeanNameTranslator implements EzyBeanNameTranslator {
	
	protected final Map<EzyBeanKey, String> map = new ConcurrentHashMap<>();
	
	@Override
	public String translate(String name, Class<?> type) {
		EzyBeanKey key = of(name, type);
		return map.containsKey(key) ? map.get(key) : name;
	}
	
	@Override
	public void map(String freename, Class<?> type, String realname) {
		map.put(of(freename, type), realname);
	}
}
