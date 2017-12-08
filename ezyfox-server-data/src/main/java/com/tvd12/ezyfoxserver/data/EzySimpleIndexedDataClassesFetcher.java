package com.tvd12.ezyfoxserver.data;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfoxserver.data.annotation.EzyIndexedData;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzySimpleIndexedDataClassesFetcher
		extends EzyLoggable
		implements EzyIndexedDataClassesFetcher {

	protected Set<Class> classes = new HashSet<>();
	
	public EzyIndexedDataClassesFetcher scan(String packageName) {
		this.classes.addAll(getAnnotatedClasses(packageName, EzyIndexedData.class));
		return this;
	}
	
	public EzyIndexedDataClassesFetcher scan(String... packageNames) {
		return scan(Arrays.asList(packageNames));
	}
	
	public EzyIndexedDataClassesFetcher scan(Iterable<String> packageNames) {
		packageNames.forEach(this::scan);
		return this;
	}
	
	@Override
	public EzyIndexedDataClassesFetcher addIndexedDataClass(Class clazz) {
		this.classes.add(clazz);
		return this;
	}
	
	@Override
	public EzyIndexedDataClassesFetcher addIndexedDataClasses(Class... classes) {
		return addIndexedDataClasses(Arrays.asList(classes));
	}
	
	@Override
	public EzyIndexedDataClassesFetcher addIndexedDataClasses(Iterable<Class> classes) {
		classes.forEach(this::addIndexedDataClass);
		return this;
	}
	
	@Override
	public Set<Class> getIndexedDataClasses() {
		return classes;
	}
	
	private Set<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annClass) {
		return EzyPackages.getAnnotatedClasses(packageName, annClass);
	}
	
}
