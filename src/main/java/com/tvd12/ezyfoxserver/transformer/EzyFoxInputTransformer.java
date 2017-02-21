package com.tvd12.ezyfoxserver.transformer;

import static com.tvd12.ezyfoxserver.util.EzyFoxDataConverter.arrayToList;
import static com.tvd12.ezyfoxserver.util.EzyFoxDataConverter.charArrayToByteArray;
import static com.tvd12.ezyfoxserver.util.EzyFoxDataConverter.charWrapperArrayToPrimitiveByteArray;
import static com.tvd12.ezyfoxserver.util.EzyFoxDataConverter.toPrimitiveByteArray;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;

import com.tvd12.ezyfoxserver.EzyFoxEnvironment;
import com.tvd12.ezyfoxserver.EzyFoxSystem;
import com.tvd12.ezyfoxserver.function.EzyFoxToObject;

public class EzyFoxInputTransformer {
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, EzyFoxToObject> TRANSFORMER;
	
	static {
		TRANSFORMER = defaultTransformers();
	}
	
	public Object transform(final Object value) {
		return  value == null 
				? transformNullValue(value) 
				: transformNonNullValue(value);
	}
	
	protected Object transformNullValue(final Object value) {
		return value;
	}
	
	protected Object transformNonNullValue(final Object value) {
		return transformNonNullValue(value, getTransformers());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(final Object value, 
			final Map<Class, EzyFoxToObject> transformers) {
		if(transformers.containsKey(value.getClass()))
			return transformers.get(value.getClass()).transform(value);
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyFoxToObject> getTransformers() {
		return TRANSFORMER;
	}
	
	private static String getDateFormatPattern() {
		return EzyFoxSystem.getEnv().getProperty(EzyFoxEnvironment.DATE_FORMAT_PATTERN);
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, EzyFoxToObject> 
			defaultTransformers() {
		Map<Class, EzyFoxToObject> answer = new HashMap<>();
		
		//primitive type
		answer.put(Boolean.class, new EzyFoxToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(Byte.class, new EzyFoxToObject<Byte>() {
			@Override
			public Object transform(Byte value) {
				return value;
			}
		});
		answer.put(Character.class, new EzyFoxToObject<Character>() {
			@Override
			public Object transform(Character value) {
				return new Byte((byte)value.charValue());
			}
		});
		answer.put(Double.class, new EzyFoxToObject<Double>() {
			@Override
			public Object transform(Double value) {
				return value;
			}
		});
		answer.put(Float.class, new EzyFoxToObject<Float>() {
			@Override
			public Object transform(Float value) {
				return value;
			}
		});
		answer.put(Integer.class, new EzyFoxToObject<Integer>() {
			@Override
			public Object transform(Integer value) {
				return value;
			}
		});
		answer.put(Long.class, new EzyFoxToObject<Long>() {
			@Override
			public Object transform(Long value) {
				return value;
			}
		});
		answer.put(Short.class, new EzyFoxToObject<Short>() {
			@Override
			public Object transform(Short value) {
				return value;
			}
		});
		answer.put(String.class, new EzyFoxToObject<String>() {
			@Override
			public Object transform(String value) {
				return value;
			}
		});
		
		//primitive array
		answer.put(boolean[].class, new EzyFoxToObject<boolean[]>() {
			@Override
			public Object transform(boolean[] value) {
				return arrayToList(value);
			}
		});
		answer.put(byte[].class, new EzyFoxToObject<byte[]>() {
			@Override
			public Object transform(byte[] value) {
				return value;
			}
		});
		answer.put(char[].class, new EzyFoxToObject<char[]>() {
			@Override
			public Object transform(char[] value) {
				return charArrayToByteArray(value);
			}
		});
		answer.put(double[].class, new EzyFoxToObject<double[]>() {
			@Override
			public Object transform(double[] value) {
				return arrayToList(value);
			}
		});
		answer.put(float[].class, new EzyFoxToObject<float[]>() {
			@Override
			public Object transform(float[] value) {
				return arrayToList(value);
			}
		});
		answer.put(int[].class, new EzyFoxToObject<int[]>() {
			@Override
			public Object transform(int[] value) {
				return arrayToList(value);
			}
		});
		answer.put(long[].class, new EzyFoxToObject<long[]>() {
			@Override
			public Object transform(long[] value) {
				return arrayToList(value);
			}
		});
		answer.put(short[].class, new EzyFoxToObject<short[]>() {
			@Override
			public Object transform(short[] value) {
				return arrayToList(value);
			}
		});
		answer.put(String[].class, new EzyFoxToObject<String[]>() {
			@Override
			public Object transform(String[] value) {
				return arrayToList(value);
			}
		});
		
		//wrapper array
		answer.put(Boolean[].class, new EzyFoxToObject<Boolean[]>() {
			@Override
			public Object transform(Boolean[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Byte[].class, new EzyFoxToObject<Byte[]>() {
			@Override
			public Object transform(Byte[] value) {
				return toPrimitiveByteArray(value);
			}
		});
		answer.put(Character[].class, new EzyFoxToObject<Character[]>() {
			@Override
			public Object transform(Character[] value) {
				return charWrapperArrayToPrimitiveByteArray(value);
			}
		});
		answer.put(Double[].class, new EzyFoxToObject<Double[]>() {
			@Override
			public Object transform(Double[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Float[].class, new EzyFoxToObject<Float[]>() {
			@Override
			public Object transform(Float[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Integer[].class, new EzyFoxToObject<Integer[]>() {
			@Override
			public Object transform(Integer[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Long[].class, new EzyFoxToObject<Long[]>() {
			@Override
			public Object transform(Long[] value) {
				return arrayToList(value);
			}
		});
		answer.put(Short[].class, new EzyFoxToObject<Short[]>() {
			@Override
			public Object transform(Short[] value) {
				return arrayToList(value);
			}
		});
		
		//other
		answer.put(Date.class, new EzyFoxToObject<Date>() {
			@Override
			public Object transform(Date value) {
				return FastDateFormat.getInstance(getDateFormatPattern()).format(value);
			}
		});
		
		return answer;
	}
	
}
