package com.tvd12.ezyfoxserver.io;

import static com.tvd12.ezyfoxserver.io.EzyDataConverter.byteArrayToCharArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.toByteWrapperArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.toCharWrapperArray;

import java.util.Map;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.function.EzyToObject;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;

public class EzyLiteOutputTransformer extends EzySimpleOutputTransformer {
	private static final long serialVersionUID = -3934914866478350107L;
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void addPrimitiveTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(byte.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.byteValue();
			}
		});
		answer.put(char.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				return EzyNumbersConverter.objectToChar(value);
			}
		});
		answer.put(double.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.doubleValue();
			}
		});
		answer.put(float.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.floatValue();
			}
		});
		answer.put(int.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.intValue();
			}
		});
		answer.put(long.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.longValue();
			}
		});
		answer.put(short.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.shortValue();
			}
		});
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void addWrapperTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean.class, new EzyToObject<Boolean>() {
			@Override
			public Object transform(Boolean value) {
				return value;
			}
		});
		answer.put(Byte.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.byteValue();
			}
		});
		answer.put(Character.class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				return EzyNumbersConverter.objectToChar(value);
			}
		});
		answer.put(Double.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.doubleValue();
			}
		});
		answer.put(Float.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.floatValue();
			}
		});
		answer.put(Integer.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.intValue();
			}
		});
		answer.put(Long.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.longValue();
			}
		});
		answer.put(Short.class, new EzyToObject<Number>() {
			@Override
			public Object transform(Number value) {
				return value.shortValue();
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
	@Override
	protected void addPrimitiveArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(boolean[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(boolean.class);
			}
		});
		answer.put(byte[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof byte[])
					return value;
				if(value instanceof String)
					return EzyBase64.decode((String)value);
				return ((EzyArray)value).toArray(byte.class);
			}
		});
		answer.put(char[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof byte[])
					return byteArrayToCharArray((byte[])value);
				if(value instanceof String)
					return ((String)value).toCharArray();
				return ((EzyArray)value).toArray(char.class);
			}
		});
		answer.put(double[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(double.class);
			}
		});
		answer.put(float[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(float.class);
			}
		});
		answer.put(int[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(int.class);
			}
		});
		answer.put(long[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(long.class);
			}
		});
		answer.put(short[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(short.class);
			}
		});
		answer.put(String[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(String.class);
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addWrapperArrayTransformers(Map<Class, EzyToObject> answer) {
		answer.put(Boolean[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Boolean.class);
			}
		});
		answer.put(Byte[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof byte[])
					return toByteWrapperArray((byte[])value);
				return ((EzyArray)value).toArray(Byte.class);
			}
		});
		answer.put(Character[].class, new EzyToObject<Object>() {
			@Override
			public Object transform(Object value) {
				if(value instanceof byte[])
					return toCharWrapperArray((byte[])value);
				return ((EzyArray)value).toArray(Character.class);
			}
		});
		answer.put(Double[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Double.class);
			}
		});
		answer.put(Float[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Float.class);
			}
		});
		answer.put(Integer[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Integer.class);
			}
		});
		answer.put(Long[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Long.class);
			}
		});
		answer.put(Short[].class, new EzyToObject<EzyArray>() {
			@Override
			public Object transform(EzyArray value) {
				return value.toArray(Short.class);
			}
		});
	}
	
}
