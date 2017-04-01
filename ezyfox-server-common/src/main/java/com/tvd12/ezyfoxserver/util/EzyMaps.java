package com.tvd12.ezyfoxserver.util;

import java.util.Map;

public abstract class EzyMaps {

	private EzyMaps() {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getValue(Map map, Class<?> type) {
		if(type == Object.class)
			return null;
		Object answer = map.get(type);
		if(answer == null && type.getInterfaces() != null)
			answer = getValueOfInterfaces(map, type);
		if(answer == null && type.getSuperclass() != null)
			answer = getValueOfSuper(map, type);
		return (T)answer;
	}
	
	@SuppressWarnings("rawtypes")
	private static Object getValueOfSuper(Map map, Class<?> type) {
		return getValue(map, type.getSuperclass());
	}
	
	@SuppressWarnings("rawtypes")
	private static Object getValueOfInterfaces(Map map, Class<?> type) {
		Object answer = null;
		for(Class<?> clazz : type.getInterfaces())
			if((answer = getValue(map, clazz)) != null)
				return answer;
		return answer;
	}
	
}
