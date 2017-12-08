package com.tvd12.ezyfoxserver.elasticsearch;

import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

@SuppressWarnings("rawtypes")
public interface EzyIndexedDataClassesBuilder 
		extends EzyBuilder<EzyIndexedDataClasses> {

	EzyIndexedDataClassesBuilder scan(String packageName);

	EzyIndexedDataClassesBuilder scan(String... packageNames);
	
	EzyIndexedDataClassesBuilder scan(Iterable<String> packageNames);
	
	EzyIndexedDataClassesBuilder addIndexedDataClass(Class clazz);
	
	EzyIndexedDataClassesBuilder addIndexedDataClasses(Class... classes);
	
	EzyIndexedDataClassesBuilder addIndexedDataClasses(Iterable<Class> classes);
	
	EzyIndexedDataClassesBuilder addIndexedDataClasses(Map<Class, EzyIndexTypes> map);
	
	EzyIndexedDataClassesBuilder addIndexedDataClass(Class clazz, EzyIndexTypes indexTypes);
	
}
