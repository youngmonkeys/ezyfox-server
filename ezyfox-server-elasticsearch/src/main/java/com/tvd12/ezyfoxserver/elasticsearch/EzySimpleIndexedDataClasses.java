package com.tvd12.ezyfoxserver.elasticsearch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.data.EzyIndexedDataClassesFetcher;
import com.tvd12.ezyfoxserver.elasticsearch.util.EzyDataIndexesAnnotations;

@SuppressWarnings("rawtypes")
public class EzySimpleIndexedDataClasses implements EzyIndexedDataClasses {
	
	protected Map<Class, EzyIndexTypes> map = new ConcurrentHashMap<>();
	
	protected EzySimpleIndexedDataClasses(Builder builder) {
		this.map.putAll(builder.indexedClassMap);
	}
	
	@Override
	public Set<Class> getIndexedClasses() {
		return new HashSet<>(map.keySet());
	}
	
	@Override
	public EzyIndexTypes getIndexTypes(Class clazz) {
		if(map.containsKey(clazz))
			return map.get(clazz);
		throw new IllegalArgumentException(clazz.getName() + " is not indexed data");
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements EzyIndexedDataClassesBuilder {
		
		protected Map<Class, EzyIndexTypes> indexedClassMap
				= new ConcurrentHashMap<>();
		protected EzyIndexedDataClassesFetcher indexedDataClassFetcher 
				= newIndexedDataClassesFetcher();
		
		@Override
		public Builder scan(String packageName) {
			this.indexedDataClassFetcher.scan(packageName);
			return this;
		}
		
		@Override
		public Builder scan(String... packageNames) {
			return scan(Arrays.asList(packageNames));
		}
		
		@Override
		public Builder scan(Iterable<String> packageNames) {
			packageNames.forEach(this::scan);
			return this;
		}
		
		@Override
		public Builder addIndexedDataClass(Class clazz) {
			this.indexedDataClassFetcher.addIndexedDataClass(clazz);
			return this;
		}
		
		@Override
		public Builder addIndexedDataClasses(Class... classes) {
			return addIndexedDataClasses(Arrays.asList(classes));
		}
		
		@Override
		public Builder addIndexedDataClasses(Iterable<Class> classes) {
			classes.forEach(this::addIndexedDataClass);
			return this;
		}
		
		@Override
		public Builder addIndexedDataClasses(Map<Class, EzyIndexTypes> map) {
			this.indexedClassMap.putAll(map);
			return this;
		}
		
		public Builder addIndexedDataClass(Class clazz, EzyIndexTypes indexTypes) {
			this.indexedClassMap.put(clazz, indexTypes);
			return this;
		}
		
		@Override
		public EzyIndexedDataClasses build() {
			this.prebuild();
			return new EzySimpleIndexedDataClasses(this);
		}
		
		protected void prebuild() {
			Set<Class> classes = indexedDataClassFetcher.getIndexedDataClasses();
			for(Class clazz : classes)
				addIndexedDataClass(clazz, EzyDataIndexesAnnotations.getIndexTypes(clazz));
		}
		
		protected EzyIndexedDataClassesFetcher newIndexedDataClassesFetcher() {
			return EzyIndexedDataClassesFetcher.newInstance();
		}
		
	}
	
}
