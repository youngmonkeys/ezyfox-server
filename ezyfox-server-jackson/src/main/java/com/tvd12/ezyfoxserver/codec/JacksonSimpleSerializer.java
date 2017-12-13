package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyParser;

public class JacksonSimpleSerializer 
		extends EzyAbstractByTypeSerializer {

	protected ObjectMapper objectMapper;
	
	public JacksonSimpleSerializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseNil(Class<T> outType) {
		return (T) writeValueAsBytes(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseWithNoParsers(Object value, Class<T> outType) {
		return (T) writeValueAsBytes(value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseWithNoParser(Object value, Class<T> outType) {
		return (T)writeValueAsBytes(value);
	}
	
	protected byte[] parseObject(Object obj) {
		return parseObject((EzyObject)obj);
	}
	
	protected byte[] writeValueAsBytes(Object obj) {
		try {
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("write value as bytes error", e);
		}
	}
	
	protected String writeValueAsString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("write value as bytes error", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void addParserss(Map<Class<?>, Map<Class<?>, EzyParser>> parserss) {
		Map<Class<?>, EzyParser> objectParsers = new ConcurrentHashMap<>();
		parserss.put(EzyObject.class, objectParsers);
		objectParsers.put(byte[].class, new EzyParser<EzyObject, byte[]>() {
			@Override
			public byte[] parse(EzyObject input) {
				return writeValueAsBytes(input.toMap());
			}
		});
		objectParsers.put(String.class, new EzyParser<EzyObject, String>() {
			@Override
			public String parse(EzyObject input) {
				return writeValueAsString(input.toMap());
			}
		});
		objectParsers.put(ByteBuffer.class, new EzyParser<EzyObject, ByteBuffer>() {
			@Override
			public ByteBuffer parse(EzyObject input) {
				return ByteBuffer.wrap(writeValueAsBytes(input.toMap()));
			}
		});
		
		Map<Class<?>, EzyParser> arrayParsers = new ConcurrentHashMap<>();
		parserss.put(EzyArray.class, arrayParsers);
		arrayParsers.put(byte[].class, new EzyParser<EzyArray, byte[]>() {
			@Override
			public byte[] parse(EzyArray input) {
				return writeValueAsBytes(input.toList());
			}
		});
		arrayParsers.put(String.class, new EzyParser<EzyArray, String>() {
			@Override
			public String parse(EzyArray input) {
				return writeValueAsString(input.toList());
			}
		});
		arrayParsers.put(ByteBuffer.class, new EzyParser<EzyArray, ByteBuffer>() {
			@Override
			public ByteBuffer parse(EzyArray input) {
				return ByteBuffer.wrap(writeValueAsBytes(input.toList()));
			}
		});
	}
	
}
