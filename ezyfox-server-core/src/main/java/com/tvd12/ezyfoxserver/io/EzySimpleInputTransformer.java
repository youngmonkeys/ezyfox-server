package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.util.EzyDataConverter.arrayToList;
import static com.tvd12.ezyfoxserver.util.EzyDataConverter.charArrayToByteArray;
import static com.tvd12.ezyfoxserver.util.EzyDataConverter.charWrapperArrayToPrimitiveByteArray;
import static com.tvd12.ezyfoxserver.util.EzyDataConverter.toPrimitiveByteArray;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;

import com.tvd12.ezyfoxserver.EzyEnvironment;
import com.tvd12.ezyfoxserver.EzySystem;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.impl.EzyArrayList;
import com.tvd12.ezyfoxserver.function.EzyToObject;

public class EzySimpleInputTransformer implements EzyInputTransformer, Serializable {
	private static final long serialVersionUID = 5436415615070699119L;
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, EzyToObject> TRANSFORMER;
	
	static {
		TRANSFORMER = defaultTransformers();
	}
	
	@Override
	public Object transform(Object value) {
		return  value == null 
				? transformNullValue(value) 
				: transformNonNullValue(value);
	}
	
	protected Object transformNullValue(Object value) {
		return value;
	}
	
	protected Object transformNonNullValue(Object value) {
		return transformNonNullValue(value, getTransformers());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(Object value, 
			final Map<Class, EzyToObject> transformers) {
		if(transformers.containsKey(value.getClass()))
			return transformers.get(value.getClass()).transform(value);
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
		answer.put(Character.class, new EzyToObject<Character>() {
			@Override
			public Object transform(Character value) {
				return new Byte((byte)value.charValue());
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
		answer.put(boolean[].class, new EzyToObject<boolean[]>() {
			@Override
			public Object transform(boolean[] value) {
				return arrayToList(value);
			}
		});
		answer.put(byte[].class, new EzyToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return value;
			}
		});
		answer.put(char[].class, new EzyToObject<char[]>() {
			@Override
			public Object transform(char[] value) {
				return charArrayToByteArray(value);
			}
		});
		answer.put(double[].class, new EzyToObject<double[]>() {
			@Override
			public Object transform(double[] value) {
				return arrayToList(value);
			}
		});
		answer.put(float[].class, new EzyToObject<float[]>() {
			@Override
			public Object transform(float[] value) {
				return arrayToList(value);
			}
		});
		answer.put(int[].class, new EzyToObject<int[]>() {
			@Override
			public Object transform(int[] value) {
				return arrayToList(value);
			}
		});
		answer.put(long[].class, new EzyToObject<long[]>() {
			@Override
			public Object transform(long[] value) {
				return arrayToList(value);
			}
		});
		answer.put(short[].class, new EzyToObject<short[]>() {
			@Override
			public Object transform(short[] value) {
				return arrayToList(value);
			}
		});
		answer.put(String[].class, new EzyToObject<String[]>() {
			@Override
			public Object transform(String[] value) {
				return arrayToList(value);
			}
		});
		
		//wrapper array
		answer.put(Boolean[].class, new EzyToObject<Boolean[]>() {
			@Override
			public Object transform(Boolean[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Byte[].class, new EzyToObject<Byte[]>() {
			@Override
			public Object transform(Byte[] value) {
				return toPrimitiveByteArray(value);
			}
		});
		answer.put(Character[].class, new EzyToObject<Character[]>() {
			@Override
			public Object transform(Character[] value) {
				return charWrapperArrayToPrimitiveByteArray(value);
			}
		});
		answer.put(Double[].class, new EzyToObject<Double[]>() {
			@Override
			public Object transform(Double[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Float[].class, new EzyToObject<Float[]>() {
			@Override
			public Object transform(Float[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Integer[].class, new EzyToObject<Integer[]>() {
			@Override
			public Object transform(Integer[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Long[].class, new EzyToObject<Long[]>() {
			@Override
			public Object transform(Long[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Short[].class, new EzyToObject<Short[]>() {
			@Override
			public Object transform(Short[] value) {
				return arrayToList(value);
			}
		});
		
		//other
		answer.put(Date.class, new EzyToObject<Date>() {
			@Override
			public Object transform(Date value) {
				return FastDateFormat.getInstance(getDateFormatPattern()).format(value);
			}
		});
		
		//me
		answer.put(EzyObject[].class, new EzyToObject<EzyObject[]>() {
			@Override
			public Object transform(EzyObject[] value) {
				EzyArray answer = new EzyArrayList();
				answer.add(value);
				return answer;
			}
		});
		
		return answer;
	}
	
}
