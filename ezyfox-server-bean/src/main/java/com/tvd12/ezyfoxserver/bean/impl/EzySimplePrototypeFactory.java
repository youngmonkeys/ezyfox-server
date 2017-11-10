package com.tvd12.ezyfoxserver.bean.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import static com.tvd12.ezyfoxserver.reflect.EzyClasses.*;
import static com.tvd12.ezyfoxserver.bean.impl.EzyBeanKey.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EzySimplePrototypeFactory
		extends EzySimpleBeanFactory
		implements EzyPrototypeFactory {

	protected final Map<EzyBeanKey, EzyPrototypeSupplier> supplierByKey
			= new ConcurrentHashMap<>();
	protected final Map<EzyPrototypeSupplier, Map> suppliersByProperties
			= new ConcurrentHashMap<>();
	
	@Override
	public EzyPrototypeSupplier getSupplier(String objectName, Class objectType) {
		String realname = translateBeanName(objectName, objectType);
		return supplierByKey.get(of(realname, objectType));
	}
	
	@Override
	public EzyPrototypeSupplier getSupplier(Map properties) {
		for(Entry<EzyPrototypeSupplier, Map> entry : suppliersByProperties.entrySet())
			if(EzyMaps.containsAll(entry.getValue(), properties))
				return entry.getKey();
		return null;
	}
	
	@Override
	public List<EzyPrototypeSupplier> getSuppliers(Map properties) {
		List<EzyPrototypeSupplier> list = new ArrayList<>();
		for(Entry<EzyPrototypeSupplier, Map> entry : suppliersByProperties.entrySet())
			if(EzyMaps.containsAll(entry.getValue(), properties))
				list.add(entry.getKey());
		return list;
	}
	
	@Override
	public List<EzyPrototypeSupplier> getSuppliers(Class annoClass) {
		List<EzyPrototypeSupplier> list = new ArrayList<>();
		for(EzyBeanKey key : supplierByKey.keySet()) {
			Class type = key.getType();
			if(type.isAnnotationPresent(annoClass))
				list.add(supplierByKey.get(type));
		}
		return list;
	}
	
	@Override
	public Map getProperties(EzyPrototypeSupplier supplier) {
		return suppliersByProperties.get(supplier);
	}
	
	@Override
	public void addSupplier(EzyPrototypeSupplier supplier) {
		Class type = supplier.getObjectType();
		addSupplier(getBeanName(type), supplier);
	}
	
	@Override
	public void addSupplier(String objectName, EzyPrototypeSupplier supplier) {
		Class<?> type = supplier.getObjectType();
		addSupplier(objectName, supplier, getProperties(type));
	}
	
	@Override
	public void addSupplier(
			String objectName, EzyPrototypeSupplier supplier, Map properties) {
		Class<?> type = supplier.getObjectType();
		EzyBeanKey key = of(objectName, type);
		
		if(supplierByKey.containsKey(key))
			return;
		
		supplierByKey.put(key, supplier);
		suppliersByProperties.put(supplier, properties);
		
		String defname = getDefaultBeanName(type);
		mapBeanName(defname, type, objectName);
		
		Set<Class> subTypes = flatSuperAndInterfaceClasses(type, true);
		for(Class<?> subType : subTypes)
			checkAndAddSupplier(objectName, subType, supplier);
	}
	
	private void checkAndAddSupplier(
			String objectName, Class<?> type, EzyPrototypeSupplier supplier) {
		EzyBeanKey key = of(objectName, type);
		if(supplierByKey.containsKey(key))
			return;
		supplierByKey.put(key, supplier);
	}
	
	private String getBeanName(Class<?> type) {
		return EzyBeanNameParser.getPrototypeName(type);
	}
	
	private Map getProperties(Class<?> type) {
		EzyPrototype ann = type.getAnnotation(EzyPrototype.class);
		Map properties = new HashMap<>();
		EzyKeyValue[] keyValues = ann != null ? ann.properties() : new EzyKeyValue[0];
		Arrays.stream(keyValues).forEach(kv -> properties.put(kv.key(), kv.value()));
		return properties;
	}
	
}
