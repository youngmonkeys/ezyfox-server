package com.tvd12.ezyfoxserver.bean.impl;

import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanKey.of;
import static com.tvd12.ezyfoxserver.reflect.EzyClasses.flatSuperAndInterfaceClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.bean.EzySingletonFactory;
import com.tvd12.ezyfoxserver.io.EzyMaps;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EzySimpleSingletonFactory
		extends EzySimpleBeanFactory
		implements EzySingletonFactory {

	protected final Map<EzyBeanKey, Object> objectsByKey
			= new ConcurrentHashMap<>();
	protected final Map<Object, Map> objectsByProperties
			= new ConcurrentHashMap<>();
	
	@Override
	public Object addSingleton(Object singleton) {
		Class type = singleton.getClass();
		return addSingleton(getBeanName(type), singleton);
	}
	
	@Override
	public Object addSingleton(String name, Object singleton) {
		Class<?> type = singleton.getClass();
		return addSingleton(name, singleton, getProperties(type));
	}
	
	@Override
	public Object addSingleton(String name, Object singleton, Map properties) {
		Class<?> type = singleton.getClass();
		EzyBeanKey key = of(name, type);
		
		if(objectsByKey.containsKey(key))
			return objectsByKey.get(key);
		
		objectsByKey.put(key, singleton);
		objectsByProperties.put(singleton, properties);
		
		String defname = getDefaultBeanName(type);
		mapBeanName(defname, type, name);
		
		Set<Class> subTypes = flatSuperAndInterfaceClasses(type, true);
		for(Class<?> subType : subTypes)
			checkAndAddSingleton(name, subType, singleton);
		return singleton;
	}
	
	private void checkAndAddSingleton(String name, Class<?> type, Object singleton) {
		EzyBeanKey key = of(name, type);
		if(objectsByKey.containsKey(key))
			return;
		objectsByKey.put(key, singleton);
	}
	
	@Override
	public Object getSingleton(String name, Class type) {
		String realname = translateBeanName(name, type);
		return objectsByKey.get(of(realname, type));
	}
	
	@Override
	public Object getSingleton(Map properties) {
		for(Entry<Object, Map> entry : objectsByProperties.entrySet())
			if(EzyMaps.containsAll(entry.getValue(), properties))
				return entry.getKey();
		return null;
	}
	
	@Override
	public List getSingletons(Map properties) {
		List list = new ArrayList<>();
		for(Entry<Object, Map> entry : objectsByProperties.entrySet())
			if(EzyMaps.containsAll(entry.getValue(), properties))
				list.add(entry.getKey());
		return list;
	}
	
	@Override
	public List getSingletons(Class annoClass) {
		List list = new ArrayList<>();
		for(EzyBeanKey key : objectsByKey.keySet()) {
			Class type = key.getType();
			if(type.isAnnotationPresent(annoClass))
				list.add(objectsByKey.get(type));
		}
		return list;
	}
	
	@Override
	public Map getProperties(Object singleton) {
		return objectsByProperties.get(singleton);
	}
	
	private String getBeanName(Class<?> type) {
		return EzyBeanNameParser.getSingletonName(type);
	}
	
	private Map getProperties(Class<?> type) {
		return EzyKeyValueParser.getSingletonProperties(type);
	}
	
}
