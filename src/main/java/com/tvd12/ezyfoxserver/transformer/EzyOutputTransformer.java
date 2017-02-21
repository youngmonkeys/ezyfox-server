package com.tvd12.ezyfoxserver.transformer;

import static com.tvd12.ezyfoxserver.util.EzyDataConverter.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.EzyEnvironment;
import com.tvd12.ezyfoxserver.EzySystem;
import com.tvd12.ezyfoxserver.function.EzyToObject;

public class EzyOutputTransformer {
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, EzyToObject> TRANSFORMER;
	
	static {
		TRANSFORMER = defaultTransformers();
	}
	
	@SuppressWarnings("rawtypes")
	public Object transform(final Object value, final Class type) {
		return  value == null 
				? transformNullValue(value) 
				: transformNonNullValue(value, type);
	}
	
	protected Object transformNullValue(final Object value) {
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Object transformNonNullValue(final Object value, final Class type) {
		return transformNonNullValue(value, type, getTransformers());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(
			final Object value, 
			final Class type,
			final Map<Class, EzyToObject> transformers) {
		if(transformers.containsKey(type))
			return transformers.get(type).transform(value);
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyToObject> getTransformers() {
		return TRANSFORMER;
	}
	
	private static String getDateFormatPattern() {
		return EzySystem.getEnv().getProperty(EzyEnvironment.DATE_FORMAT_PATTERN);
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, EzyToObject> 
			defaultTransformers() {
		Map<Class, EzyToObject> answer = new HashMap<>();
		
		//primitive type
		answer.put(Boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(Byte.class, new EzyToObject<Byte>() {
			@Override
			public Object transform(Byte value) {
				return value;
			}
		});
		answer.put(Character.class, new EzyToObject<Byte>() {
			@Override
			public Object transform(Byte value) {
				return (char)value.byteValue();
			}
		});
		answer.put(Double.class, new EzyToObject<Double>() {
			@Override
			public Object transform(Double value) {
				return value;
			}
		});
		answer.put(Float.class, new EzyToObject<Float>() {
			@Override
			public Object transform(Float value) {
				return value;
			}
		});
		answer.put(Integer.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value;
			}
		});
		answer.put(Long.class, new EzyToObject<Long>() {
			@Override
			public Object transform(Long value) {
				return value;
			}
		});
		answer.put(Short.class, new EzyToObject<Short>() {
			@Override
			public Object transform(Short value) {
				return value;
			}
		});
		answer.put(String.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				return value;
			}
		});
		
		//primitive array
		answer.put(boolean[].class, new EzyToObject<Collection<Boolean>>() {
			@Override
			public Object transform(Collection<Boolean> value) {
				return collectionToPrimitiveBoolArray(value);
			}
		});
		answer.put(byte[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return value;
			}
		});
		answer.put(char[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return byteArrayToCharArray(value);
			}
		});
		answer.put(double[].class, new EzyToObject<Collection<Double>>() {
			@Override
			public Object transform(Collection<Double> value) {
				return collectionToPrimitiveDoubleArray(value);
			}
		});
		answer.put(float[].class, new EzyToObject<Collection<Float>>() {
			@Override
			public Object transform(Collection<Float> value) {
				return collectionToPrimitiveFloatArray(value);
			}
		});
		answer.put(int[].class, new EzyToObject<Collection<Integer>>() {
			@Override
			public Object transform(Collection<Integer> value) {
				return collectionToPrimitiveIntArray(value);
			}
		});
		answer.put(long[].class, new EzyToObject<Collection<Long>>() {
			@Override
			public Object transform(Collection<Long> value) {
				return collectionToPrimitiveLongArray(value);
			}
		});
		answer.put(short[].class, new EzyToObject<Collection<Short>>() {
			@Override
			public Object transform(Collection<Short> value) {
				return collectionToPrimitiveShortArray(value);
			}
		});
		answer.put(String[].class, new EzyToObject<Collection<String>>() {
			@Override
			public Object transform(Collection<String> value) {
				return collectionToStringArray(value);
			}
		});
		
		//wrapper array
		answer.put(Boolean[].class, new EzyToObject<Collection<Boolean>>() {
			@Override
			public Object transform(Collection<Boolean> value) {
				return collectionToWrapperBoolArray(value);
			}
		});
		answer.put(Character[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return toCharWrapperArray(value);
			}
		});
		answer.put(Double[].class, new EzyToObject<Collection<Double>>() {
			@Override
			public Object transform(Collection<Double> value) {
				return collectionToWrapperDoubleArray(value);
			}
		});
		answer.put(Float[].class, new EzyToObject<Collection<Float>>() {
			@Override
			public Object transform(Collection<Float> value) {
				return collectionToWrapperFloatArray(value);
			}
		});
		answer.put(Integer[].class, new EzyToObject<Collection<Integer>>() {
			@Override
			public Object transform(Collection<Integer> value) {
				return collectionToWrapperIntArray(value);
			}
		});
		answer.put(Long[].class, new EzyToObject<Collection<Long>>() {
			@Override
			public Object transform(Collection<Long> value) {
				return collectionToWrapperLongArray(value);
			}
		});
		answer.put(Short[].class, new EzyToObject<Collection<Short>>() {
			@Override
			public Object transform(Collection<Short> value) {
				return collectionToWrapperShortArray(value);
			}
		});
		
		//other
		answer.put(Date.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				try {
					return FastDateFormat.getInstance(getDateFormatPattern()).parse(value);
				} catch (ParseException e) {
					getLogger().info("value = " + value + " is invalid", e);
				}
				return null;
			}
		});
		
		return answer;
	}
	
	private static Logger getLogger() {
		return LoggerFactory.getLogger(EzyOutputTransformer.class);
	}
	
}
