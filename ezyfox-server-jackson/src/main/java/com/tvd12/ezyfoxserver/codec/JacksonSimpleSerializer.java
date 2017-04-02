package com.tvd12.ezyfoxserver.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyParser;

public class JacksonSimpleSerializer 
		extends EzyAbstractSerializer 
		implements EzyMessageSerializer {

	protected ObjectMapper objectMapper;
	
	public JacksonSimpleSerializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	protected byte[] parseNil() {
		return null;
	}
	
	@Override
	protected byte[] parseWithNoParser(Object value) {
		return writeValueAsBytes(value);
	}
	
	protected byte[] parseObject(Object obj) {
		return parseObject((EzyObject)obj);
	}
	
	protected byte[] parseObject(EzyObject obj) {
		return writeValueAsBytes(obj.toMap());
	}
	
	protected byte[] parseArray(Object obj) {
		return parseArray((EzyArray)obj);
	}
	
	protected byte[] parseArray(EzyArray array) {
		return writeValueAsBytes(array.toList());
	}
	
	protected byte[] writeValueAsBytes(Object obj) {
		try {
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("write value as bytes error", e);
		}
	}
	
	protected Map<Class<?>, EzyParser<Object, byte[]>> defaultParsers() {
		Map<Class<?>, EzyParser<Object, byte[]>> parsers = new ConcurrentHashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers) {
		parsers.put(EzyObject.class, this::parseObject);
		parsers.put(EzyArray.class, this::parseArray);
	}
	
}
