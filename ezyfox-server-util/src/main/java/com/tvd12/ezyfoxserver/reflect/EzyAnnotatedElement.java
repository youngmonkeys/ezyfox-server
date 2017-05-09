package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;

public interface EzyAnnotatedElement {

	<T extends Annotation> T getAnnotation(Class<T> annClass);
	
	boolean isAnnotated(Class<? extends Annotation> annClass);
	
}
