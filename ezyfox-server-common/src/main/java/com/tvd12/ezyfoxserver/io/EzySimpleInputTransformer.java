package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.io.EzyDataConverter.arrayToList;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.charArrayToByteArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.charWrapperArrayToPrimitiveByteArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.toPrimitiveByteArray;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyToObject;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class EzySimpleInputTransformer
		extends EzyEntityBuilders
		implements EzyInputTransformer, Serializable {
	private static final long serialVersionUID = 5436415615070699119L;
	
	@SuppressWarnings("rawtypes")
	protected final Map<Class, EzyToObject> transformers = defaultTransformers();
	
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
	
	@SuppressWarnings("rawtypes")
	protected Object transformNonNullValue(
			Object value, Map<Class, EzyToObject> transformers) {
		return transformNonNullValue(value, value.getClass(), transformers);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object transformNonNullValue(
			Object value, Class type, Map<Class, EzyToObject> transformers) {
		EzyToObject trans = transformers.get(type);
		if(trans != null)
			return trans.transform(value);
		else if(type.isEnum())
			return value.toString();
		else if(value instanceof Map)
			return transformMap(value);
		else if(value instanceof EzyBuilder)
			return transformBuilder(value, transformers);
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Object transformMap(Object value) {
		return newObjectBuilder().append((Map)value).build();
	}
	
	@SuppressWarnings("rawtypes")
	protected Object transformBuilder(
			Object value, Map<Class, EzyToObject> transformers) {
		return transformNonNullValue(((EzyBuilder)value).build());
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyToObject> getTransformers() {
		return transformers;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyToObject> defaultTransformers() {
		Map<Class, EzyToObject> answer = new ConcurrentHashMap<>();
		addOtherTransformers(answer);
		addEntityTransformers(answer);
		addWrapperTransformers(answer);
		addWrapperArrayTransformers(answer);
		addPrimitiveArrayTransformers(answer);
		addTwoDimensionsWrapperArrayTransformers(answer);
		addTwoDimensionsPrimitiveArrayTransformers(answer);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addWrapperTransformers(Map<Class, EzyToObject> answer) {
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
	}
	
	@SuppressWarnings("rawtypes")
	protected void addPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
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
	}
	
	@SuppressWarnings("rawtypes")
	protected void addWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
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
		
	}
	
	//primitive two dimensions array
	@SuppressWarnings("rawtypes")
	protected void addTwoDimensionsPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean[][].class, new EzyToObject<boolean[][]>() {
			@Override
			public Object transform(boolean[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(byte[][].class, new EzyToObject<byte[][]>() {
			@Override
			public Object transform(byte[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(char[][].class, new EzyToObject<char[][]>() {
			@Override
			public Object transform(char[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(double[][].class, new EzyToObject<double[][]>() {
			@Override
			public Object transform(double[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(float[][].class, new EzyToObject<float[][]>() {
			@Override
			public Object transform(float[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(int[][].class, new EzyToObject<int[][]>() {
			@Override
			public Object transform(int[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(long[][].class, new EzyToObject<long[][]>() {
			@Override
			public Object transform(long[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(short[][].class, new EzyToObject<short[][]>() {
			@Override
			public Object transform(short[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
	}
	
	//wrapper two dimensions array
	@SuppressWarnings("rawtypes")
	protected void addTwoDimensionsWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean[][].class, new EzyToObject<Boolean[][]>() {
			@Override
			public Object transform(Boolean[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Byte[][].class, new EzyToObject<Byte[][]>() {
			@Override
			public Object transform(Byte[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Character[][].class, new EzyToObject<Character[][]>() {
			@Override
			public Object transform(Character[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Double[][].class, new EzyToObject<Double[][]>() {
			@Override
			public Object transform(Double[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Float[][].class, new EzyToObject<Float[][]>() {
			@Override
			public Object transform(Float[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Integer[][].class, new EzyToObject<Integer[][]>() {
			@Override
			public Object transform(Integer[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Long[][].class, new EzyToObject<Long[][]>() {
			@Override
			public Object transform(Long[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(Short[][].class, new EzyToObject<Short[][]>() {
			@Override
			public Object transform(Short[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(String[][].class, new EzyToObject<String[][]>() {
			@Override
			public Object transform(String[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
	}
	
	//entity
	@SuppressWarnings("rawtypes")
	protected void addEntityTransformers(Map<Class, EzyToObject> answer) {
		answer.put(EzyObject[].class, new EzyToObject<EzyObject[]>() {
			@Override
			public Object transform(EzyObject[] value) {
				return newArrayBuilder().append(value).build();
			}
		});
		answer.put(EzyObject[][].class, new EzyToObject<EzyObject[][]>() {
			@Override
			public Object transform(EzyObject[][] value) {
				return newArrayBuilder().append(value).build();
			}
		});
	}
	
	//other
	@SuppressWarnings("rawtypes")
	protected void addOtherTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Date.class, new EzyToObject<Date>() {
			@Override
			public Object transform(Date value) {
				return EzyDates.format(value);
			}
		});
		
		answer.put(LocalDate.class, new EzyToObject<LocalDate>() {
			@Override
			public Object transform(LocalDate value) {
				return EzyDates.format(value, "yyyy-MM-dd");
			}
		});
		
		answer.put(LocalDateTime.class, new EzyToObject<LocalDateTime>() {
			@Override
			public Object transform(LocalDateTime value) {
				return EzyDates.format(value);
			}
		});
		
		answer.put(Class.class, new EzyToObject<Class>() {
			@Override
			public Object transform(Class value) {
				return value.getName();
			}
		});
		
	}
	
}
