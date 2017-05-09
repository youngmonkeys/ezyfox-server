package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import static com.tvd12.ezyfoxserver.database.util.EzyMapstoreAnnotations.getMapName;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyBeanContextAware;
import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.database.annotation.EzyMapstore;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzyBeanMapstoreCreator 
		extends EzyLoggable
		implements EzyMapstoreCreator, EzyBeanContextAware {

	protected Map<String, MapStore> mapstores = new ConcurrentHashMap<>();
	
	@Override
	public Set<String> getMapNames() {
		return new HashSet<>(mapstores.keySet());
	}

	@Override
	public MapStore create(String mapName, Properties properties) {
		return mapstores.get(mapName);
	}
	
	@SuppressWarnings("unchecked")
	public void setContext(EzyBeanContext context) {
		EzySingletonFactory singletonFactory = context.getSingletonFactory();
		List<Object> objects = singletonFactory.getSingletons(EzyMapstore.class);
		for(Object object : objects)
			mapstores.put(getMapName(object.getClass()), (MapStore) object);
	}
	
}
