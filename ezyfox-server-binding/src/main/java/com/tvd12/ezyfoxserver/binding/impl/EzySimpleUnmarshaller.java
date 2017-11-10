package com.tvd12.ezyfoxserver.binding.impl;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnwrapper;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyTypes;
import com.tvd12.ezyfoxserver.util.EzyCollectionFactory;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.util.EzyMapFactory;

public class EzySimpleUnmarshaller
		extends EzyEntityBuilders
		implements EzyUnmarshaller {

	@SuppressWarnings("rawtypes")
	protected final Map<Class, EzyReader> readersByType;
	@SuppressWarnings("rawtypes")
	protected final Map<Class, EzyReader> readersByObjectType;
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyUnwrapper> unwrappersByObjectType;
	
	protected final EzyMapFactory mapFactory = new EzyMapFactory();
	protected final EzyCollectionFactory collectionFactory = new EzyCollectionFactory();
	
	public EzySimpleUnmarshaller() {
		this.readersByObjectType = defaultReaders();
		this.readersByType = defaultReadersByType();
		this.unwrappersByObjectType  = new ConcurrentHashMap<>();
	}
	
	@SuppressWarnings("rawtypes")
	public void addReader(EzyReader reader) {
		readersByType.put(reader.getClass(), reader);
	}
	
	@SuppressWarnings("rawtypes")
	public void addReaders(Iterable<EzyReader> readers) {
		readers.forEach(this::addReader);
	}
	
	@SuppressWarnings("rawtypes")
	public void addReader(Class type, EzyReader reader) {
		readersByObjectType.put(type, reader);
		readersByType.put(reader.getClass(), reader);
	}
	
	@SuppressWarnings("rawtypes")
	public void addReaders(Map<Class, EzyReader> readers) {
		readers.keySet().forEach(key -> addReader(key, readers.get(key)));
	}
	
	@SuppressWarnings("rawtypes")
	public void addUnwrapper(Class type, EzyUnwrapper unwrapper) {
		unwrappersByObjectType.put(type, unwrapper);
	}
	
	@SuppressWarnings("rawtypes")
	public void addUnwrappers(Map<Class, EzyUnwrapper> unwrappers) {
		unwrappers.keySet().forEach(key -> addUnwrapper(key, unwrappers.get(key)));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void unwrap(Object value, Object output) {
		unwrappersByObjectType.get(output.getClass()).unwrap(this, value, output);;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> T unmarshal(Object value, Class<T> outType) {
		if(value == null)
			return null;
		EzyReader reader = EzyMaps.getValue(readersByObjectType, outType);
		if(reader != null)
			return (T) reader.read(this, value);
		if(outType.isArray())
			return (T) readArray((EzyArray)value, outType.getComponentType());
		throw new IllegalArgumentException("has no reader for " + outType);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Map<K, V> unmarshalMap(
			Object value, Class mapType, Class<K> keyType, Class<V> valueType) {
		Map map = mapFactory.newMap(mapType);
		EzyObject object = (EzyObject)value;
		for(Object key : object.keySet())
			map.put(unmarshal(key, keyType), unmarshal((Object)object.get(key), valueType));
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public <T> Collection<T> unmarshalCollection(
			Object value, Class collectionType, Class<T> itemType) {
		if(value instanceof Collection)
			return unmarshalCollection(((Collection)value).iterator(), collectionType, itemType);
		return unmarshalCollection(((EzyArray)value).iterator(), collectionType, itemType);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Collection<T> unmarshalCollection(
			Iterator iterator, Class collectionType, Class<T> itemType) {
		Collection<T> collection = collectionFactory.newCollection(collectionType);
		while(iterator.hasNext())
			collection.add(unmarshal(iterator.next(), itemType));
		return collection;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T unmarshal(Class<? extends EzyReader> readerClass, Object value) {
		if(readersByType.containsKey(readerClass))
			return (T) readersByType.get(readerClass).read(this, value);
		throw new IllegalArgumentException("can't unmarshal value " + 
			value + ", " + readerClass.getName() + " is not reader class");
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyReader> defaultReadersByType() {
		Map<Class, EzyReader> map = new ConcurrentHashMap<>();
		readersByObjectType.values().forEach(w -> map.put(w.getClass(), w));
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyReader> defaultReaders() {
		Map<Class, EzyReader> map = new ConcurrentHashMap<>();
		Set<Class> normalTypes = EzyTypes.ALL_TYPES;
		for(Class normalType : normalTypes)
			map.put(normalType, defaultReader());
		map.put(Date.class, defaultReader());
		map.put(Class.class, defaultReader());
		map.put(LocalDate.class, defaultReader());
		map.put(LocalDateTime.class, defaultReader());
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private EzyReader defaultReader() {
		return (um, o) -> o;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object[] readArray(EzyArray array, Class componentType) {
		Object[] answer = (Object[]) Array.newInstance(componentType, array.size());
		for(int i = 0 ; i < array.size() ; i++) 
			answer[i] = unmarshal((Object)array.get(i), componentType);
		return answer;
	}
	
}
