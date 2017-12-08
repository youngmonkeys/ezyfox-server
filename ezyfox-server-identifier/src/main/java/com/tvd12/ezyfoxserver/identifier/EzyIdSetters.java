package com.tvd12.ezyfoxserver.identifier;

import java.util.Map;

public interface EzyIdSetters {

	EzyIdSetter getIdSetter(Class<?> clazz);
	
	Map<Class<?>, EzyIdSetter> getIdSetters();
	
}
