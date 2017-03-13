package com.tvd12.ezyfoxserver.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.function.EzyToObject;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class EzyLiteCollectionConverter {

	private EzyLiteCollectionConverter() {
	}
	
	private static final Map<Class, EzyToObject> CONVERTERS;
	
	static {
		CONVERTERS = defaultConverters();
	}
	
	public static <T> T toArray(Collection coll, Class type) {
		if(CONVERTERS.containsKey(type))
			return (T) CONVERTERS.get(type).transform(coll);
		throw new IllegalStateException("can not convert " + type);
	}
	
	private static Map<Class, EzyToObject> defaultConverters() {
		Map<Class, EzyToObject> answer = new HashMap<>();
		
		answer.put(boolean[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveBoolArray(input);
			}
		});
		
		answer.put(byte[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveBytes(input);
			}
		});
		answer.put(char[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveChars(input);
			}
		});
		answer.put(double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveDoubles(input);
			}
		});
		answer.put(float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveFloats(input);
			}
		});
		answer.put(int[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveInts(input);
			}
		});
		answer.put(long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveLongs(input);
			}
		});
		answer.put(short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveShorts(input);
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
				return EzyNumbersConverter.numbersToWrapperBytes(input);
			}
		});
		answer.put(Character[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperChars(input);
			}
		});
		answer.put(Double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperDoubles(input);
			}
		});
		answer.put(Float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperFloats(input);
			}
		});
		answer.put(Integer[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperInts(input);
			}
		});
		answer.put(Long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperLongs(input);
			}
		});
		answer.put(Short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperShorts(input);
			}
		});
		
		return answer;
	}
	
}
