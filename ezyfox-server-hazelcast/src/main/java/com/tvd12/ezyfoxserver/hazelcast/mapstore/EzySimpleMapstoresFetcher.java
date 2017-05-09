package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import static com.tvd12.ezyfoxserver.database.util.EzyMapstoreAnnotations.getMapName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.database.annotation.EzyMapstore;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleMapstoresFetcher
		extends EzyLoggable
		implements EzyMapstoresFetcher {
	
	protected Map<String, Object> mapstores = new ConcurrentHashMap<>();
	
	protected EzySimpleMapstoresFetcher(Builder builder) {
		this.mapstores.putAll(builder.mapstores);
	}

	@Override
	public Set<String> getMapNames() {
		return new HashSet<>(mapstores.keySet());
	}
	
	@Override
	public Object getMapstore(String mapName) {
		return mapstores.get(mapName);
	}
	
	@Override
	public Map<String, Object> getMapstores() {
		return new HashMap<>(mapstores);
	}
	
	@Override
	public boolean containsMapstore(String mapName) {
		return mapstores.containsKey(mapName);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@SuppressWarnings({"rawtypes"})
	public static class Builder
			extends EzyLoggable
			implements EzyBuilder<EzyMapstoresFetcher> {

		protected Map<String, Object> mapstores = new HashMap<>();
		protected Map<String, Class<?>> mapstoreClassMap = new HashMap<>();
		
		public Builder scan(String packageName) {
			Set<Class<?>> classes = getMapstoreClasses(packageName);;
			classes.forEach(this::addMapstoreClass);
			return this;
		}
		
		public Builder scan(String... packageNames) {
			return scan(Sets.newHashSet(packageNames));
		}
		
		public Builder scan(Iterable<String> packageNames) {
			packageNames.forEach(this::scan);
			return this;	
		}
		
		public Builder addMapstoreClass(Class clazz) {
			return addMapstoreClass(() -> getMapName(clazz), clazz);
		}
		
		public Builder addMapstoreClass(String mapName, Class clazz) {
			return addMapstoreClass(() -> mapName, clazz);
		}
		
		public Builder addMapstore(MapStore mapstore) {
			return addMapstore(getMapName(mapstore.getClass()), mapstore);
		}
		
		public Builder addMapstore(String mapName, MapStore mapstore) {
			this.mapstores.put(mapName, mapstore);
			getLogger().debug("add mapstore {} of class {}", mapName, mapstore.getClass().getName());
			return this;
		}
		
		@Override
		public EzySimpleMapstoresFetcher build() {
			for(String mapName : mapstoreClassMap.keySet()) {
				Class clazz = mapstoreClassMap.get(mapName);
				Object mapstore = newMapstore(clazz);
				addMapstore(mapName, (MapStore) mapstore);
			}
			return new EzySimpleMapstoresFetcher(this);
		}
		
		protected Object newMapstore(Class<?> mapstoreClass) {
			return EzyClasses.newInstance(mapstoreClass);
		}
		
		private Set<Class<?>> getMapstoreClasses(String packageName) {
			return EzyPackages.getAnnotatedClasses(packageName, EzyMapstore.class);
		}
		
		private Builder addMapstoreClass(Supplier<String> mapNameSupplier, Class clazz) {
			if(MapStore.class.isAssignableFrom(clazz))
				this.mapstoreClassMap.put(mapNameSupplier.get(), clazz);
			else
				getLogger().warn("{} doesn't implements {}, ignore it", clazz, MapStore.class);
			return this;
		}
		
	}
	
}
