package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToStringArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.boolArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.boolArrayWrapperToPrimitive;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.boolArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.boolArraysWrapperToPrimitive;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.byteArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.byteArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.charArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.charArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.doubleArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.doubleArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.floatArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.floatArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.intArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.intArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.longArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.longArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveByteArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveBytes;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveCharArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveChars;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveDoubleArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveDoubles;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveFloatArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveFloats;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveIntArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveInts;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveLongArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveLongs;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveShortArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToPrimitiveShorts;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperByteArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperBytes;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperCharArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperChars;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperDoubleArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperDoubles;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperFloatArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperFloats;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperIntArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperInts;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperLongArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperLongs;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperShortArrays;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.numbersToWrapperShorts;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.shortArrayPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.shortArraysPrimitiveToWrapper;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToChar;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveBoolArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveByteArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveByteArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveCharArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveCharArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveDoubleArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveFloatArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveIntArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveIntArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveLongArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveLongArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveShortArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToPrimitiveShortArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToStringArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperBoolArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperByteArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperByteArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperCharArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperCharArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperDoubleArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperFloatArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperIntArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperIntArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperLongArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperLongArrays;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperShortArray;
import static com.tvd12.ezyfoxserver.io.EzyStringConveter.stringToWrapperShortArrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.function.EzyToObject;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzySimpleValueConverter 
		extends EzyLoggable
		implements EzyValueConverter {

	protected final Map<Class, EzyToObject> transformers;
	
	public EzySimpleValueConverter() {
		this.transformers = defaultTransformers();
	}
	
	@Override
	public <T> T convert(Object value, Class<T> outType) {
		if(value == null)
			return null;
		if(transformers.containsKey(outType))
			return (T) transformers.get(outType).transform(value);
		return (T)value;
	}
	
	protected IllegalArgumentException 
			newTransformerException(Class<?> type, Object value) {
		return new IllegalArgumentException(
				"can't transform: " + value + " to " + type.getSimpleName() + " value");
}

	//tank
	private Map<Class, EzyToObject> 
		defaultTransformers() {
		Map<Class, EzyToObject> answer = new ConcurrentHashMap<>();
		addOtherTransformers(answer);
		addWrapperTransformers(answer);
		addPrimitiveTransformers(answer);
		addWrapperArrayTransformers(answer);
		addPrimitiveArrayTransformers(answer);
		addTwoDimensionsWrapperArrayTransformers(answer);
		addTwoDimensionsPrimitiveArrayTransformers(answer);
		return answer;
	}

	protected void addPrimitiveTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Boolean.valueOf((String)value);
				if(value instanceof Boolean)
					return (Boolean)value;
				throw newTransformerException(boolean.class, value);
			}
		});
		answer.put(byte.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Byte.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).byteValue();
				throw newTransformerException(byte.class, value);
			}
		});
		answer.put(char.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof Character)
					return ((Character)value);
				if(value instanceof Number)
					return (char)((Number)value).byteValue();
				if(value instanceof String)
					return stringToChar((String)value);
				throw newTransformerException(char.class, value);
			}
		});
		answer.put(double.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Double.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).doubleValue();
				throw newTransformerException(double.class, value);
			}
		});
		answer.put(float.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Float.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).floatValue();
				throw newTransformerException(float.class, value);
			}
		});
		answer.put(int.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Integer.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).intValue();
				throw newTransformerException(int.class, value);
			}
		});
		answer.put(long.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Long.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).longValue();
				throw newTransformerException(long.class, value);
			}
		});
		answer.put(short.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Short.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).shortValue();
				throw newTransformerException(short.class, value);
			}
		});
	}

	protected void addWrapperTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Boolean.valueOf((String)value);
				if(value instanceof Boolean)
					return (Boolean)value;
				throw newTransformerException(Boolean.class, value);
			}
		});
		answer.put(Byte.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Byte.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).byteValue();
				throw newTransformerException(Byte.class, value);
			}
		});
		answer.put(Character.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof Character)
					return ((Character)value);
				if(value instanceof Number)
					return (char)((Number)value).byteValue();
				if(value instanceof String) 
					return stringToChar((String)value);
				throw newTransformerException(Character.class, value);
			}
		});
		answer.put(Double.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Double.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).doubleValue();
				throw newTransformerException(Double.class, value);
			}
		});
		answer.put(Float.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Float.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).floatValue();
				throw newTransformerException(float.class, value);
			}
		});
		answer.put(Integer.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Integer.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).intValue();
				throw newTransformerException(Integer.class, value);
			}
		});
		answer.put(Long.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Long.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).longValue();
				throw newTransformerException(Long.class, value);
			}
		});
		answer.put(Short.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return Short.valueOf((String)value);
				if(value instanceof Number)
					return ((Number)value).shortValue();
				throw newTransformerException(Short.class, value);
			}
		});
		
		answer.put(String.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				return value.toString();
			}
		});
	}

	protected void addPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveBoolArray((String)value);
				if(value instanceof boolean[])
					return value;
				if(value instanceof Boolean[])
					return boolArrayWrapperToPrimitive((Boolean[])value);
				if(value instanceof Collection)
					return collectionToPrimitiveBoolArray((Collection<Boolean>)value);
				throw newTransformerException(boolean[].class, value);
			}
		});
		answer.put(byte[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveByteArray((String)value);
				if(value instanceof byte[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveBytes((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveBytes((Collection)value);
				throw newTransformerException(byte[].class, value);
			}
		});
		answer.put(char[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveCharArray((String)value);
				if(value instanceof char[])
					return value;
				if(value instanceof Object[])
					return numbersToPrimitiveChars((Object[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveChars((Collection)value);
				throw newTransformerException(char[].class, value);
			}
		});

		answer.put(double[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveDoubleArray((String)value);
				if(value instanceof double[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveDoubles((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveDoubles((Collection)value);
				throw newTransformerException(double[].class, value);
			}
		});
		answer.put(float[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveFloatArray((String)value);
				if(value instanceof float[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveFloats((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveFloats((Collection)value);
				throw newTransformerException(float[].class, value);
			}
		});
		answer.put(int[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveIntArray((String)value);
				if(value instanceof int[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveInts((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveInts((Collection)value);
				throw newTransformerException(int[].class, value);
			}
		});
		answer.put(long[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveLongArray((String)value);
				if(value instanceof long[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveLongs((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveLongs((Collection)value);
				throw newTransformerException(long[].class, value);
			}
		});
		answer.put(short[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveShortArray((String)value);
				if(value instanceof short[])
					return value;
				if(value instanceof Number[])
					return numbersToPrimitiveShorts((Number[])value);
				if(value instanceof Collection)
					return numbersToPrimitiveShorts((Collection)value);
				throw newTransformerException(short[].class, value);
			}
		});
	}
	protected void addWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperBoolArray((String)value);
				if(value instanceof Boolean[])
					return value;
				if(value instanceof boolean[])
					return boolArrayPrimitiveToWrapper((boolean[])value);
				if(value instanceof Collection)
					return collectionToWrapperBoolArray((Collection<Boolean>)value);
				throw newTransformerException(Boolean[].class, value);
			}
		});
		answer.put(Byte[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperByteArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperBytes((Number[])value);
				if(value instanceof byte[])
					return byteArrayPrimitiveToWrapper((byte[])value);
				if(value instanceof Collection)
					return numbersToWrapperBytes((Collection)value);
				throw newTransformerException(Boolean[].class, value);
			}
		});
		answer.put(Character[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperCharArray((String)value);
				if(value instanceof Character[])
					return value;
				if(value instanceof char[])
					return charArrayPrimitiveToWrapper((char[])value);
				if(value instanceof Collection)
					return numbersToWrapperChars((Collection)value);
				throw newTransformerException(Character[].class, value);
			}
		});
		answer.put(Double[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperDoubleArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperDoubles((Number[])value);
				if(value instanceof double[])
					return doubleArrayPrimitiveToWrapper((double[])value);
				if(value instanceof Collection)
					return numbersToWrapperDoubles((Collection)value);
				throw newTransformerException(Double[].class, value);
			}
		});
		answer.put(Float[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperFloatArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperFloats((Number[])value);
				if(value instanceof float[])
					return floatArrayPrimitiveToWrapper((float[])value);
				if(value instanceof Collection)
					return numbersToWrapperFloats((Collection)value);
				throw newTransformerException(Float[].class, value);
			}
		});
		answer.put(Integer[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperIntArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperInts((Number[])value);
				if(value instanceof int[])
					return intArrayPrimitiveToWrapper((int[])value);
				if(value instanceof Collection)
					return numbersToWrapperInts((Collection)value);
				throw newTransformerException(Integer[].class, value);
			}
		});
		answer.put(Long[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperLongArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperLongs((Number[])value);
				if(value instanceof long[])
					return longArrayPrimitiveToWrapper((long[])value);
				if(value instanceof Collection)
					return numbersToWrapperLongs((Collection)value);
				throw newTransformerException(Long[].class, value);
			}
		});
		answer.put(Short[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperShortArray((String)value);
				if(value instanceof Number[])
					return numbersToWrapperShorts((Number[])value);
				if(value instanceof short[])
					return shortArrayPrimitiveToWrapper((short[])value);
				if(value instanceof Collection)
					return numbersToWrapperShorts((Collection)value);
				throw newTransformerException(Short[].class, value);
			}
		});
		
		answer.put(String[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return ((String)value).split(",");
				if(value instanceof String[])
					return value;
				if(value instanceof Collection)
					return collectionToStringArray((Collection<String>)value);
				throw newTransformerException(String[].class, value);
			}
		});
	}

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

	protected void addTwoDimensionsPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveBoolArrays((String)value);
				if(value instanceof boolean[][])
					return value;
				if(value instanceof Boolean[][])
					return boolArraysWrapperToPrimitive((Boolean[][])value);
				throw newTransformerException(boolean[][].class, value);
			}
		});
		answer.put(byte[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveByteArrays((String)value);
				if(value instanceof byte[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveByteArrays((Number[][])value);
				throw newTransformerException(byte[][].class, value);
			}
		});
		answer.put(char[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveCharArrays((String)value);
				if(value instanceof char[][])
					return value;
				if(value instanceof Object[][])
					return numbersToPrimitiveCharArrays((Object[][])value);
				throw newTransformerException(char[][].class, value);
			}
		});
		answer.put(double[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveDoubleArrays((String)value);
				if(value instanceof double[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveDoubleArrays((Number[][])value);
				throw newTransformerException(double[][].class, value);
			}
		});
		answer.put(float[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveFloatArrays((String)value);
				if(value instanceof float[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveFloatArrays((Number[][])value);
				throw newTransformerException(float[][].class, value);
			}
		});
		answer.put(int[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveIntArrays((String)value);
				if(value instanceof int[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveIntArrays((Number[][])value);
				throw newTransformerException(boolean[][].class, value);
			}
		});
		answer.put(long[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveLongArrays((String)value);
				if(value instanceof long[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveLongArrays((Number[][])value);
				throw newTransformerException(long[][].class, value);
			}
		});
		answer.put(short[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToPrimitiveShortArrays((String)value);
				if(value instanceof short[][])
					return value;
				if(value instanceof Number[][])
					return numbersToPrimitiveShortArrays((Number[][])value);
				throw newTransformerException(short[][].class, value);
			}
		});
	}

	protected void addTwoDimensionsWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperBoolArrays((String)value);
				if(value instanceof Boolean[][])
					return value;
				if(value instanceof boolean[][])
					return boolArraysPrimitiveToWrapper((boolean[][])value);
				throw newTransformerException(Boolean[][].class, value);
			}
		});
		answer.put(Byte[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperByteArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperByteArrays((Number[][])value);
				if(value instanceof byte[][])
					return byteArraysPrimitiveToWrapper((byte[][])value);
				throw newTransformerException(Byte[][].class, value);
			}
		});
		answer.put(Character[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperCharArrays((String)value);
				if(value instanceof Object[][])
					return numbersToWrapperCharArrays((Number[][])value);
				if(value instanceof char[][])
					return charArraysPrimitiveToWrapper((char[][])value);
				throw newTransformerException(Character[][].class, value);
			}
		});
		answer.put(Double[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperDoubleArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperDoubleArrays((Number[][])value);
				if(value instanceof double[][])
					return doubleArraysPrimitiveToWrapper((double[][])value);
				throw newTransformerException(Double[][].class, value);
			}
		});
		answer.put(Float[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperFloatArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperFloatArrays((Number[][])value);
				if(value instanceof float[][])
					return floatArraysPrimitiveToWrapper((float[][])value);
				throw newTransformerException(Float[][].class, value);
			}
		});
		answer.put(Integer[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperIntArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperIntArrays((Number[][])value);
				if(value instanceof int[][])
					return intArraysPrimitiveToWrapper((int[][])value);
				throw newTransformerException(Integer[][].class, value);
			}
		});
		answer.put(Long[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperLongArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperLongArrays((Number[][])value);
				if(value instanceof long[][])
					return longArraysPrimitiveToWrapper((long[][])value);
				throw newTransformerException(Long[][].class, value);
			}
		});
		answer.put(Short[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToWrapperShortArrays((String)value);
				if(value instanceof Number[][])
					return numbersToWrapperShortArrays((Number[][])value);
				if(value instanceof short[][])
					return shortArraysPrimitiveToWrapper((short[][])value);
				throw newTransformerException(Short[][].class, value);
			}
		});
		answer.put(String[][].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof String)
					return stringToStringArrays((String)value);
				if(value instanceof String[][])
					return value;
				throw newTransformerException(String[][].class, value);
			}
		});
	}
}
