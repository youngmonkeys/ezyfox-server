package com.tvd12.ezyfoxserver.io;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyToObject;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzySimpleCollectionConverter implements EzyCollectionConverter {

	private final Map<Class, EzyToObject> converters = defaultConverters();
	
	@Override
	public <T> T toArray(Collection coll, Class type) {
		return convert(coll, type);
	}
	
	protected <T> T convert(Object value, Class type) {
		if(converters.containsKey(type))
			return (T) converters.get(type).transform(value);
		throw new IllegalArgumentException("can not convert " + type);
	}
	
	protected <T> T toArray(Object array, Class type) {
		return toArray((Collection)array, type.getComponentType());
	}
	
	private <T> T[] toArray(Iterable iterable, T[] array) {
		int count = 0;
		Class arrayType = array.getClass().getComponentType();
		for(Object obj : iterable)
			array[count ++] = toArray(obj, arrayType);
		return array;
	}
	
	private char[] toPrimitiveCharArray(Object value) {
		if(value instanceof byte[])
			return EzyDataConverter.byteArrayToCharArray((byte[])value);
		else if(value instanceof EzyArray)
			return ((EzyArray)value).toArray(char.class);
		else
			return EzyNumbersConverter.numbersToPrimitiveChars((Collection)value);
	}
	
	private Character[] toWrapperCharArray(Object value) {
		if(value instanceof byte[])
			return EzyDataConverter.toCharWrapperArray((byte[])value);
		else if(value instanceof EzyArray)
			return ((EzyArray)value).toArray(Character.class);
		else
			return EzyNumbersConverter.numbersToWrapperChars((Collection)value);
	}
	
	private byte[] toPrimitiveByteArray(Object value) {
		if(value instanceof byte[]) 
			return (byte[])value;
		else if(value instanceof EzyArray)
			return ((EzyArray)value).toArray(byte.class);
		else if(value instanceof String)
			return EzyBase64.decode((String)value);
		return EzyDataConverter.collectionToPrimitiveByteArray((Collection<Byte>)value);
	}
	
	private Byte[] toWrapperByteArray(Object value) {
		if(value instanceof byte[])
			return EzyDataConverter.toByteWrapperArray((byte[])value);
		else if(value instanceof EzyArray)
			return ((EzyArray)value).toArray(Byte.class);
		else if(value instanceof String)
			return EzyDataConverter.toByteWrapperArray(EzyBase64.decode((String)value));
		return EzyDataConverter.collectionToWrapperByteArray((Collection<Byte>)value);
	}
	
	private Map<Class, EzyToObject> defaultConverters() {
		Map<Class, EzyToObject> converters= new ConcurrentHashMap<>();
		addDefaultConverters(converters);
		addCustomConverters(converters);
		return converters;
	}
	
	private void addDefaultConverters(Map<Class, EzyToObject> converters) {
		converters.put(boolean.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToPrimitiveBoolArray(input);
			}
		});
		
		converters.put(byte.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveBytes(input);
			}
		});
		converters.put(char.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveChars(input);
			}
		});
		converters.put(double.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveDoubles(input);
			}
		});
		converters.put(float.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveFloats(input);
			}
		});
		converters.put(int.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveInts(input);
			}
		});
		converters.put(long.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveLongs(input);
			}
		});
		converters.put(short.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToPrimitiveShorts(input);
			}
		});
		
		converters.put(String.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToStringArray(input);
			}
		});
		
		// wrapper
		converters.put(Boolean.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyDataConverter.collectionToWrapperBoolArray(input);
			}
		});
		
		converters.put(Byte.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperBytes(input);
			}
		});
		converters.put(Character.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperChars(input);
			}
		});
		converters.put(Double.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperDoubles(input);
			}
		});
		converters.put(Float.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperFloats(input);
			}
		});
		converters.put(Integer.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperInts(input);
			}
		});
		converters.put(Long.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperLongs(input);
			}
		});
		converters.put(Short.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyNumbersConverter.numbersToWrapperShorts(input);
			}
		});
		
		//entity
		converters.put(EzyObject.class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return EzyCollections.toArray(input, size -> new EzyObject[size]);
			}
		});
	}
	
	//=============
	//=============
	
	private void addCustomConverters(Map<Class, EzyToObject> converters) {
		converters.put(boolean[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new boolean[input.size()][]);
			}
		});
		
		converters.put(byte[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				int count = 0;
				byte[][] answer = new byte[input.size()][];
				for(Object item : input)
					answer[count ++] = toPrimitiveByteArray(item);
				return answer;
			}
		});
		converters.put(char[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				int count = 0;
				char[][] answer = new char[input.size()][];
				for(Object item : input)
					answer[count ++] = toPrimitiveCharArray(item);
				return answer;
			}
		});
		converters.put(double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new double[input.size()][]);
			}
		});
		converters.put(float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new float[input.size()][]);
			}
		});
		converters.put(int[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new int[input.size()][]);
			}
		});
		converters.put(long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new long[input.size()][]);
			}
		});
		converters.put(short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new short[input.size()][]);
			}
		});
		
		converters.put(String[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new String[input.size()][]);
			}
		});
		
		// wrapper
		converters.put(Boolean[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Boolean[input.size()][]);
			}
		});
		
		converters.put(Byte[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				int count = 0;
				Byte[][] answer = new Byte[input.size()][];
				for(Object item : input)
					answer[count ++] = toWrapperByteArray(item);
				return answer;
			}
		});
		converters.put(Character[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				int count = 0;
				Character[][] answer = new Character[input.size()][];
				for(Object item : input)
					answer[count ++] = toWrapperCharArray(item);
				return answer;
			}
		});
		converters.put(Double[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Double[input.size()][]);
			}
		});
		converters.put(Float[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Float[input.size()][]);
			}
		});
		converters.put(Integer[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Integer[input.size()][]);
			}
		});
		converters.put(Long[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Long[input.size()][]);
			}
		});
		converters.put(Short[].class, new EzyToObject<Collection>() {
			@Override
			public Object transform(Collection input) {
				return toArray(input, new Short[input.size()][]);
			}
		});
		
	}
	
}
