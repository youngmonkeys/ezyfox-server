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
	
	public Object transform(final Object input) {
		return  input == null 
				? transformNullValue(input) 
				: transformNonNullValue(input);
	}
	
	protected Object transformNullValue(final Object input) {
		return input;
	}
	
	protected Object transformNonNullValue(final Object input) {
		return transformNonNullValue(input, getTransformers());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(final Object input, 
			final Map<Class, EzyFoxToObject> transformers) {
		if(transformers.containsKey(input.getClass()))
			return transformers.get(input.getClass()).transform(input);
		return input;
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
			public Object transform(Boolean input) {
				return input;
			}
		});
		answer.put(Byte.class, new EzyFoxToObject<Byte>() {
			@Override
			public Object transform(Byte input) {
				return input;
			}
		});
		answer.put(Character.class, new EzyFoxToObject<Character>() {
			@Override
			public Object transform(Character input) {
				return (byte)input.charValue();
			}
		});
		answer.put(Double.class, new EzyFoxToObject<Double>() {
			@Override
			public Object transform(Double input) {
				return input;
			}
		});
		answer.put(Float.class, new EzyFoxToObject<Float>() {
			@Override
			public Object transform(Float input) {
				return input;
			}
		});
		answer.put(Integer.class, new EzyFoxToObject<Integer>() {
			@Override
			public Object transform(Integer input) {
				return input;
			}
		});
		answer.put(Long.class, new EzyFoxToObject<Long>() {
			@Override
			public Object transform(Long input) {
				return input;
			}
		});
		answer.put(Short.class, new EzyFoxToObject<Short>() {
			@Override
			public Object transform(Short input) {
				return input;
			}
		});
		answer.put(String.class, new EzyFoxToObject<String>() {
			@Override
			public Object transform(String input) {
				return input;
			}
		});
		
		//primitive array
		answer.put(boolean[].class, new EzyFoxToObject<boolean[]>() {
			@Override
			public Object transform(boolean[] input) {
				return arrayToList(input);
			}
		});
		answer.put(byte[].class, new EzyFoxToObject<byte[]>() {
			@Override
			public Object transform(byte[] input) {
				return input;
			}
		});
		answer.put(char[].class, new EzyFoxToObject<char[]>() {
			@Override
			public Object transform(char[] input) {
				return charArrayToByteArray(input);
			}
		});
		answer.put(double[].class, new EzyFoxToObject<double[]>() {
			@Override
			public Object transform(double[] input) {
				return arrayToList(input);
			}
		});
		answer.put(float[].class, new EzyFoxToObject<float[]>() {
			@Override
			public Object transform(float[] input) {
				return arrayToList(input);
			}
		});
		answer.put(int[].class, new EzyFoxToObject<int[]>() {
			@Override
			public Object transform(int[] input) {
				return arrayToList(input);
			}
		});
		answer.put(long[].class, new EzyFoxToObject<long[]>() {
			@Override
			public Object transform(long[] input) {
				return arrayToList(input);
			}
		});
		answer.put(short[].class, new EzyFoxToObject<short[]>() {
			@Override
			public Object transform(short[] input) {
				return arrayToList(input);
			}
		});
		answer.put(String[].class, new EzyFoxToObject<String[]>() {
			@Override
			public Object transform(String[] input) {
				return arrayToList(input);
			}
		});
		
		//wrapper array
		answer.put(Boolean[].class, new EzyFoxToObject<Boolean[]>() {
			@Override
			public Object transform(Boolean[] input) {
				return arrayToList(input);
			}
		});
		answer.put(Byte[].class, new EzyFoxToObject<Byte[]>() {
			@Override
			public Object transform(Byte[] input) {
				return toPrimitiveByteArray(input);
			}
		});
		answer.put(Character[].class, new EzyFoxToObject<Character[]>() {
			@Override
			public Object transform(Character[] input) {
				return charWrapperArrayToPrimitiveByteArray(input);
			}
		});
		answer.put(Double[].class, new EzyFoxToObject<Double[]>() {
			@Override
			public Object transform(Double[] input) {
				return arrayToList(input);
			}
		});
		answer.put(Float[].class, new EzyFoxToObject<Float[]>() {
			@Override
			public Object transform(Float[] input) {
				return arrayToList(input);
			}
		});
		answer.put(Integer[].class, new EzyFoxToObject<Integer[]>() {
			@Override
			public Object transform(Integer[] input) {
				return arrayToList(input);
			}
		});
		answer.put(Long[].class, new EzyFoxToObject<Long[]>() {
			@Override
			public Object transform(Long[] input) {
				return arrayToList(input);
			}
		});
		answer.put(Short[].class, new EzyFoxToObject<Short[]>() {
			@Override
			public Object transform(Short[] input) {
				return arrayToList(input);
			}
		});
		
		//other
		answer.put(Date.class, new EzyFoxToObject<Date>() {
			@Override
			public Object transform(Date input) {
				return FastDateFormat.getInstance(getDateFormatPattern()).format(input);
			}
		});
		
		return answer;
	}
	
}
