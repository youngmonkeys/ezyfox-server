package com.tvd12.ezyfoxserver.bean;

import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.properties.EzyPropertiesReader;

@SuppressWarnings("rawtypes")
public interface EzyBeanContextBuilder extends EzyBuilder<EzyBeanContext> {

	EzyBeanContextBuilder scan(String packageName);

	EzyBeanContextBuilder scan(String... packageNames);

	EzyBeanContextBuilder scan(Iterable<String> packageNames);

	EzyBeanContextBuilder addSingleton(String name, Object singleton);

	EzyBeanContextBuilder addSingletonClass(Class clazz);

	EzyBeanContextBuilder addSingletonClasses(Class... classes);

	EzyBeanContextBuilder addSingletonClasses(Iterable<Class> classes);

	EzyBeanContextBuilder addPrototypeClass(Class clazz);

	EzyBeanContextBuilder addPrototypeClasses(Class... classes);

	EzyBeanContextBuilder addPrototypeClasses(Iterable<Class> classes);
	
	EzyBeanContextBuilder addPrototypeSupplier(String objectName, EzyPrototypeSupplier supplier);
	
	EzyBeanContextBuilder errorHandler(EzyErrorHandler handler);
	
	EzyBeanContextBuilder addProperties(Map properties);
	
	EzyBeanContextBuilder addProperty(String key, Object value);
	
	EzyBeanContextBuilder propertiesReader(EzyPropertiesReader propertiesReader);

}