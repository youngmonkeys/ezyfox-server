package com.tvd12.ezyfoxserver.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.function.EzyToObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class EzyCollectionConverter {

	private static final Map<Class, EzyToObject> CONVERTERS;
	
	static {
		CONVERTERS = defaultConverters();
	}
	
	private EzyCollectionConverter() {
	}
	
	public static <T> T toArray(Collection coll, Class type) {
		if(CONVERTERS.containsKey(type))
			return (T) CONVERTERS.get(type).transform(coll);
		throw new IllegalStateException("can not convert " + type);
	}
	
	private static Map<Class, EzyToObject> defaultConverters() {
		Map<Class, EzyToObject> answer = new HashMap<>();
		
		// primitive
		answer.put(boolean[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveBoolArray(input);
			}
		});
		answer.put(byte[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveByteArray(input);
			}
		});
		answer.put(char[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveCharArray(input);
			}
		});
		answer.put(double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveDoubleArray(input);
			}
		});
		answer.put(float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveFloatArray(input);
			}
		});
		answer.put(int[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveIntArray(input);
			}
		});
		answer.put(long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveLongArray(input);
			}
		});
		answer.put(short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveShortArray(input);
			}
		});
		answer.put(String[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToStringArray(input);
			}
		});
		
		// wrapper
		answer.put(Boolean[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperBoolArray(input);
			}
		});
		answer.put(Byte[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperByteArray(input);
			}
		});
		answer.put(Character[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperCharArray(input);
			}
		});
		answer.put(Double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperDoubleArray(input);
			}
		});
		answer.put(Float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperFloatArray(input);
			}
		});
		answer.put(Integer[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperIntArray(input);
			}
		});
		answer.put(Long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperLongArray(input);
			}
		});
		answer.put(Short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperShortArray(input);
			}
		});
		
		return answer;
	}
	
}
