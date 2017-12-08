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
public class EzySimpleIdSetters extends EzyLoggable implements EzyIdSetters {

	protected Map<Class<?>, EzyIdSetter> entityIdSetters = new ConcurrentHashMap<>();
	
	protected EzySimpleIdSetters(Builder builder) {
		this.entityIdSetters.putAll(builder.entityIdSetters);
	}
	
	@Override
	public EzyIdSetter getIdSetter(Class<?> clazz) {
		if(entityIdSetters.containsKey(clazz))
			return entityIdSetters.get(clazz);
		throw new IllegalArgumentException("has no id setter for " + clazz);
	}
	
	@Override
	public Map<Class<?>, EzyIdSetter> getIdSetters() {
		return new HashMap<>(entityIdSetters);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyIdSetters> {

		protected Set<Class> entityClasses = new HashSet<>();
		protected Map<Class<?>, EzyIdSetter> entityIdSetters = new HashMap<>();

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

		public Builder addIdSetter(Class<?> clazz, EzyIdSetter setter) {
			this.entityIdSetters.put(clazz, setter);
			return this;
		}

		public Builder addIdSetters(Map<Class<?>, EzyIdSetter> setters) {
			for (Class<?> key : setters.keySet())
				this.addIdSetter(key, setters.get(key));
			return this;
		}

		@Override
		public EzyIdSetters build() {
			this.prebuild();
			return new EzySimpleIdSetters(this);
		}
		
		protected void prebuild() {
			parseEntityClasses();
		}
		
		protected void parseEntityClasses() {
			for (Class<?> entityClass : entityClasses) {
				EzyIdSetter setter = newIdSetter(entityClass);
				entityIdSetters.put(entityClass, setter);
			}
		}

		protected boolean isHasIdClass(Class<?> clazz) {
			return EzyHasIdEntity.class.isAssignableFrom(clazz);
		}

		protected Set<Class<? extends Annotation>> getAnnotationClasses() {
			return new HashSet<>();
		}

		protected EzyIdSetter newIdSetter(Class<?> clazz) {
			EzyIdSetterImplementer implementer = newIdSetterImplementer(clazz);
			return implementer.implement();
		}

		protected EzyIdSetterImplementer newIdSetterImplementer(Class<?> clazz) {
			return new EzySimpleIdSetterImplementer(clazz);
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
