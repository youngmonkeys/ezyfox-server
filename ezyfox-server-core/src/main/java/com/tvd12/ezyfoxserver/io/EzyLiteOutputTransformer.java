package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.util.EzyDataConverter.byteArrayToCharArray;
import static com.tvd12.ezyfoxserver.util.EzyDataConverter.toByteWrapperArray;
import static com.tvd12.ezyfoxserver.util.EzyDataConverter.toCharWrapperArray;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.EzyEnvironment;
import com.tvd12.ezyfoxserver.EzySystem;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyToObject;

public class EzyLiteOutputTransformer implements EzyOutputTransformer, Serializable {
	private static final long serialVersionUID = 8067491929651725682L;
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, EzyToObject> TRANSFORMER;
	
	static {
		TRANSFORMER = defaultTransformers();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object transform(Object value, Class type) {
		return  value == null 
				? transformNullValue(value) 
				: transformNonNullValue(value, type);
	}
	
	protected Object transformNullValue(Object value) {
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Object transformNonNullValue(Object value, Class type) {
		return transformNonNullValue(value, type, getTransformers());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(
			Object value, Class type, Map<Class, EzyToObject> transformers) {
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
		answer.put(boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(byte.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value.byteValue();
			}
		});
		answer.put(char.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return (char)value.intValue();
			}
		});
		answer.put(double.class, new EzyToObject<Double>() {
			@Override
			public Object transform(Double value) {
				return value;
			}
		});
		answer.put(float.class, new EzyToObject<Float>() {
			@Override
			public Object transform(Float value) {
				return value;
			}
		});
		answer.put(int.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value;
			}
		});
		answer.put(long.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.longValue();
			}
		});
		answer.put(short.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value.shortValue();
			}
		});
		
		//primitive type
		answer.put(Boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(Byte.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value.byteValue();
			}
		});
		answer.put(Character.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return (char)value.intValue();
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
		answer.put(Long.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.longValue();
			}
		});
		answer.put(Short.class, new EzyToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value.shortValue();
			}
		});
		answer.put(String.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				return value;
			}
		});
		
		//primitive array
		answer.put(boolean[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(boolean[].class);
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
		answer.put(double[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(double[].class);
			}
		});
		answer.put(float[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(float[].class);
			}
		});
		answer.put(int[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(int[].class);
			}
		});
		answer.put(long[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(long[].class);
			}
		});
		answer.put(short[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(short[].class);
			}
		});
		answer.put(String[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(String[].class);
			}
		});
		
		//wrapper array
		answer.put(Boolean[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Boolean[].class);
			}
		});
		answer.put(Byte[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return toByteWrapperArray(value);
			}
		});
		answer.put(Character[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return toCharWrapperArray(value);
			}
		});
		answer.put(Double[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Double[].class);
			}
		});
		answer.put(Float[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Float[].class);
			}
		});
		answer.put(Integer[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Integer[].class);
			}
		});
		answer.put(Long[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Long[].class);
			}
		});
		answer.put(Short[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Short[].class);
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
		
		//me
		answer.put(EzyObject[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				EzyObject[] answer = new EzyObject[value.size()];
				for(int i = 0 ; i < value.size() ; i++) 
					answer[i] = value.get(i, EzyObject.class);
				return answer;
			}
		});
		
		return answer;
	}
	
	private static Logger getLogger() {
		return LoggerFactory.getLogger(EzyLiteOutputTransformer.class);
	}
	
}
