package com.tvd12.ezyfoxserver.identifier;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyHasIdEntity;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzySimpleIdFetchers extends EzyLoggable implements EzyIdFetchers {

	protected Map<Class<?>, EzyIdFetcher> entityIdFetchers = new ConcurrentHashMap<>();
	
	protected EzySimpleIdFetchers(Builder builder) {
		this.entityIdFetchers.putAll(builder.entityIdFetchers);
	}
	
	@Override
	public EzyIdFetcher getIdFetcher(Class<?> clazz) {
		if(entityIdFetchers.containsKey(clazz))
			return entityIdFetchers.get(clazz);
		throw new IllegalArgumentException("has no id fetcher for " + clazz);
	}
	
	@Override
	public Map<Class<?>, EzyIdFetcher> getIdFetchers() {
		return new HashMap<>(entityIdFetchers);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyIdFetchers> {

		protected Set<Class> entityClasses = new HashSet<>();
		protected Map<Class<?>, EzyIdFetcher> entityIdFetchers = new HashMap<>();

		public Builder scan(String packageName) {
			this.entityClasses.addAll(getHasIdClasses(packageName));
			this.entityClasses.addAll(getAnnotatedClasses(packageName));
			return this;
		}

		public Builder scan(String... packageNames) {
			return scan(Sets.newHashSet(packageNames));
		}

		public Builder scan(Iterable<String> packageNames) {
			packageNames.forEach(this::scan);
			return this;
		}

		public Builder addClass(Class clazz) {
			if (isHasIdClass(clazz) || isAnnotatedClass(clazz))
				this.entityClasses.add(clazz);
			return this;
		}

		public Builder addClasses(Class... classes) {
			return addClasses(Sets.newHashSet(classes));
		}

		public Builder addClasses(Iterable<Class> classes) {
			classes.forEach(this::addClass);
			return this;
		}

		public Builder addIdFetcher(Class<?> clazz, EzyIdFetcher fetcher) {
			this.entityIdFetchers.put(clazz, fetcher);
			return this;
		}

		public Builder addIdFetchers(Map<Class<?>, EzyIdFetcher> fetchers) {
			for (Class<?> key : fetchers.keySet())
				this.addIdFetcher(key, fetchers.get(key));
			return this;
		}

		@Override
		public EzyIdFetchers build() {
			this.prebuild();
			return new EzySimpleIdFetchers(this);
		}
		
		protected void prebuild() {
			parseEntityClasses();
		}
		
		protected void parseEntityClasses() {
			for (Class<?> entityClass : entityClasses) {
				EzyIdFetcher fetcher = newIdFetcher(entityClass);
				entityIdFetchers.put(entityClass, fetcher);
			}
		}

		protected boolean isHasIdClass(Class<?> clazz) {
			return EzyHasIdEntity.class.isAssignableFrom(clazz);
		}

		protected Set<Class<? extends Annotation>> getAnnotationClasses() {
			return new HashSet<>();
		}

		protected EzyIdFetcher newIdFetcher(Class<?> clazz) {
			EzyIdFetcherImplementer implementer = newIdFetcherImplementer(clazz);
			return implementer.implement();
		}

		protected EzyIdFetcherImplementer newIdFetcherImplementer(Class<?> clazz) {
			return new EzySimpleIdFetcherImplementer(clazz);
		}

		protected boolean isAnnotatedClass(Class<?> clazz) {
			for (Class<? extends Annotation> annClass : getAnnotationClasses())
				if (clazz.isAnnotationPresent(annClass))
					return true;
			return false;
		}

		protected Set<Class<?>> getAnnotatedClasses(String packageName) {
			Set<Class<?>> classes = new HashSet<>();
			for (Class<? extends Annotation> annClass : getAnnotationClasses())
				classes.addAll(EzyPackages.getAnnotatedClasses(packageName, annClass));
			return classes;
		}

		protected Set<Class<?>> getHasIdClasses(String packageName) {
			return EzyPackages.getExtendsClasses(packageName, EzyHasIdEntity.class);
		}

	}
}
