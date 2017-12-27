package com.tvd12.ezyfoxserver.binding.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyTypes;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class EzySimpleMarshaller
		extends EzyEntityBuilders
		implements EzyMarshaller {

	@SuppressWarnings("rawtypes")
	protected final Map<Class, EzyWriter> writersByType;
	@SuppressWarnings("rawtypes")
	protected final Map<Class, EzyWriter> writersByObjectType;
	
	public EzySimpleMarshaller() {
		this.writersByObjectType = defaultWriters();
		this.writersByType = defaultWritersByType();
	}
	
	@SuppressWarnings("rawtypes")
	public void addWriter(EzyWriter writer) {
		writersByType.put(writer.getClass(), writer);
	}
	
	@SuppressWarnings("rawtypes")
	public void addWriters(Iterable<EzyWriter> writers) {
		writers.forEach(this::addWriter);
	}
	
	@SuppressWarnings("rawtypes")
	public void addWriter(Class type, EzyWriter writer) {
		writersByObjectType.put(type, writer);
		writersByType.put(writer.getClass(), writer);
	}
	
	@SuppressWarnings("rawtypes")
	public void addWriters(Map<Class, EzyWriter> writers) {
		writers.keySet().forEach(key -> addWriter(key, writers.get(key)));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> T marshal(Object object) {
		if(object == null)
			return null;
		Class objectType = object.getClass();
		EzyWriter writer = EzyMaps.getValue(writersByObjectType, objectType);
		if(writer != null)
			return (T) writer.write(this, object);
		if(objectType.isEnum())
			return (T) object.toString();
		if(objectType.isArray())
			return (T) writeArray((Object[])object);
		throw new IllegalArgumentException("has no writer for " + object.getClass());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T marshal(Class<? extends EzyWriter> writerClass, Object object) {
		if(writersByType.containsKey(writerClass))
			return (T) writersByType.get(writerClass).write(this, object);
		throw new IllegalArgumentException("can't marshal object " + 
			object + ", " + writerClass.getName() + " is not writer class");
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyWriter> defaultWritersByType() {
		Map<Class, EzyWriter> map = new ConcurrentHashMap<>();
		writersByObjectType.values().forEach(w -> map.put(w.getClass(), w));
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyWriter> defaultWriters() {
		Map<Class, EzyWriter> map = new ConcurrentHashMap<>();
		Set<Class> normalTypes = EzyTypes.ALL_TYPES;
		for(Class normalType : normalTypes)
			map.put(normalType, defaultWriter());
		map.put(Date.class, defaultWriter());
		map.put(Class.class, defaultWriter());
		map.put(LocalDate.class, defaultWriter());
		map.put(LocalDateTime.class, defaultWriter());
		map.put(EzyArray.class, defaultWriter());
		map.put(EzyObject.class, defaultWriter());
		map.put(Map.class, mapWriter());
		map.put(HashMap.class, mapWriter());
		map.put(TreeMap.class, mapWriter());
		map.put(ConcurrentHashMap.class, mapWriter());
		map.put(List.class, collectionWriter());
		map.put(Set.class, collectionWriter());
		map.put(ArrayList.class, collectionWriter());
		map.put(HashSet.class, collectionWriter());
		map.put(Collection.class, collectionWriter());
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	private EzyWriter defaultWriter() {
		return (m, o) -> o;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private EzyWriter mapWriter() {
		return (m, o) -> {
			EzyObjectBuilder builder = newObjectBuilder();
			Map map = (Map)o;
			map.entrySet().forEach(e -> writeEntry(builder, (Entry) e));
			return builder.build();
		};
	} 
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private EzyWriter collectionWriter() {
		return (m, o) -> {
			EzyArrayBuilder builder = newArrayBuilder();
			Collection collection = (Collection)o;
			collection.forEach(i -> builder.append((Object)marshal(i)));
			return builder.build();
		};
	}
	
	private EzyArray writeArray(Object[] array) {
		EzyArrayBuilder builder = newArrayBuilder();
		Arrays.stream(array).forEach(o -> builder.append((Object)marshal(o)));
		return builder.build();
	}
	
	@SuppressWarnings("rawtypes")
	private void writeEntry(EzyObjectBuilder builder, Entry entry) {
		builder.append(marshal(entry.getKey()), (Object)marshal(entry.getValue()));
	}

}
