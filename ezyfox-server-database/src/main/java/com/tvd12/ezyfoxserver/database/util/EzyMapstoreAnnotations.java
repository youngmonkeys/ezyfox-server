package com.tvd12.ezyfoxserver.database.util;

import static com.tvd12.ezyfoxserver.reflect.EzyClasses.getVariableName;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.tvd12.ezyfoxserver.database.annotation.EzyMapstore;

public final class EzyMapstoreAnnotations {

	private EzyMapstoreAnnotations() {
	}
	
	public static String getMapName(Class<?> clazz) {
		EzyMapstore anno = clazz.getAnnotation(EzyMapstore.class);
		String name = anno.value();
		return isEmpty(name) ? getVariableName(clazz) : name;
	}
	
}
