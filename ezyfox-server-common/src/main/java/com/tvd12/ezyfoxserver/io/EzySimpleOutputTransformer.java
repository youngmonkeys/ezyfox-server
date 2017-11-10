package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.io.EzyDataConverter.byteArrayToCharArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveIntArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveLongArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveShortArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToStringArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperIntArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperLongArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperShortArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.toCharWrapperArray;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyToObject;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleOutputTransformer
		extends EzyLoggable
		implements EzyOutputTransformer, Serializable {
	private static final long serialVersionUID = 8067491929651725682L;
	
	@SuppressWarnings("rawtypes")
	private final Map<Class, EzyToObject> transformers = defaultTransformers();
	
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
		EzyToObject trans = transformers.get(type);
		if(trans != null)
			return transformers.get(type).transform(value);
		if(type.isEnum())
			return Enum.valueOf(type, value.toString());
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, EzyToObject> getTransformers() {
		return transformers;
	}
	
	//tank
	@SuppressWarnings("rawtypes")
	private Map<Class, EzyToObject> 
			defaultTransformers() {
		Map<Class, EzyToObject> answer = new ConcurrentHashMap<>();
		addOtherTransformers(answer);
		addEntityTransformers(answer);
		addWrapperTransformers(answer);
		addPrimitiveTransformers(answer);
		addWrapperArrayTransformers(answer);
		addPrimitiveArrayTransformers(answer);
		addTwoDimensionsWrapperArrayTransformers(answer);
		addTwoDimensionsPrimitiveArrayTransformers(answer);
		return answer;
	}
	
	protected EzyObject[] toObjectArray(EzyArray value) {
		EzyObject[] answer = new EzyObject[value.size()];
		for(int i = 0 ; i < value.size() ; i++) 
			answer[i] = value.get(i, EzyObject.class);
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addPrimitiveTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(byte.class, new EzyToObject<Byte>() {
			@Override
			public Object transform(Byte value) {
				return value;
			}
		});
		answer.put(char.class, new EzyToObject<Byte>() {
			@Override
			public Object transform(Byte value) {
				return (char)value.byteValue();
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
		answer.put(long.class, new EzyToObject<Long>() {
			@Override
			public Object transform(Long value) {
				return value;
			}
		});
		answer.put(short.class, new EzyToObject<Short>() {
			@Override
			public Object transform(Short value) {
				return value;
			}
		});
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
	}
	
	@SuppressWarnings("rawtypes")
	protected void addPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
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
	}
	
	@SuppressWarnings("rawtypes")
	protected void addWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
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
	}
	
	@SuppressWarnings("rawtypes")
	protected void addOtherTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Date.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				try {
					return EzyDates.parse(value);
				} catch (Exception e) {
					getLogger().info("value = " + value + " is invalid", e);
				}
				return null;
			}
		});
		
		answer.put(LocalDate.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				try {
					return EzyDates.parseDate(value, "yyyy-MM-dd");
				} catch (Exception e) {
					getLogger().info("value = " + value + " is invalid", e);
				}
				return null;
			}
		});
		
		answer.put(LocalDateTime.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				try {
					return EzyDates.parseDateTime(value);
				} catch (Exception e) {
					getLogger().info("value = " + value + " is invalid", e);
				}
				return null;
			}
		});
		
		//other
		answer.put(Class.class, new EzyToObject<String>() {
			@Override
			public Object transform(String value) {
				try {
					return EzyClasses.getClass(value);
				} catch (Exception e) {
					getLogger().info("value = " + value + " is invalid", e);
				}
				return null;
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	protected void addEntityTransformers(Map<Class, EzyToObject> answer) {
		answer.put(EzyObject[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return toObjectArray(value);
			}
		});
		
		answer.put(EzyObject[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				EzyObject[][] answer = new EzyObject[value.size()][];
				for(int i = 0 ; i < value.size() ; i++)
					answer[i] = toObjectArray(value.get(i, EzyArray.class));
				return answer;
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	protected void addTwoDimensionsPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(boolean[].class);
			}
		});
		answer.put(byte[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(byte[].class);
			}
		});
		answer.put(char[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(char[].class);
			}
		});
		answer.put(double[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(double[].class);
			}
		});
		answer.put(float[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(float[].class);
			}
		});
		answer.put(int[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(int[].class);
			}
		});
		answer.put(long[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(long[].class);
			}
		});
		answer.put(short[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(short[].class);
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	protected void addTwoDimensionsWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Boolean[].class);
			}
		});
		answer.put(Byte[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Byte[].class);
			}
		});
		answer.put(Character[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Character[].class);
			}
		});
		answer.put(Double[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Double[].class);
			}
		});
		answer.put(Float[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Float[].class);
			}
		});
		answer.put(Integer[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Integer[].class);
			}
		});
		answer.put(Long[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Long[].class);
			}
		});
		answer.put(Short[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Short[].class);
			}
		});
		answer.put(String[][].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(String[].class);
			}
		});
	}
	
}
