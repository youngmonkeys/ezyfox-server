package com.tvd12.ezyfoxserver.hazelcast.util;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;

public final class EzyMapServiceAutoImplAnnotations {

	private EzyMapServiceAutoImplAnnotations() {
	}
	
	public static String getBeanName(Class<?> annotatedClass) {
		EzyMapServiceAutoImpl anno = annotatedClass.getAnnotation(EzyMapServiceAutoImpl.class);
		String beanName = anno.name().trim();
		return StringUtils.isEmpty(beanName) ? EzyClasses.getVariableName(annotatedClass) : beanName;
	}
	
}
