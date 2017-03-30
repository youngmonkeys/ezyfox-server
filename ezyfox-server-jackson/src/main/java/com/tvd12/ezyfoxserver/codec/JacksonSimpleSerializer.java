package com.tvd12.ezyfoxserver.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.util.EzyMaps;

public class JacksonSimpleSerializer 
		extends JacksonObjectMapperSetter 
		implements EzyMessageSerializer {

	protected Map<Class<?>, Parser> parsers;
	
	{
		parsers = defaultParsers();
	}
	
	public JacksonSimpleSerializer(ObjectMapper objectMapper) {
		super(objectMapper);
	}
	
	@Override
	public byte[] serialize(Object value) {
		return tryParse(value);
	}
	
	protected byte[] tryParse(Object value) {
		return value == null ? parseNull() : parseNotNull(value);
	}
	
	protected byte[] parseNull() {
		return null;
	}
	
	protected byte[] parseNotNull(Object value) {
		return parseNotNull(getParser(value.getClass()), value);
	}
	
	protected byte[] parseNotNull(Parser parser, Object value) {
		return parser != null ? parser.parse(value) : writeValueAsBytes(value);
	}
	
	protected Parser getParser(Class<?> type) {
		return EzyMaps.getValue(parsers, type);
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
	
	protected Map<Class<?>, Parser> defaultParsers() {
		Map<Class<?>, Parser> parsers = new ConcurrentHashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected void addParsers(Map<Class<?>, Parser> parsers) {
		parsers.put(EzyObject.class, this::parseObject);
		parsers.put(EzyArray.class, this::parseArray);
	}
	
	private static interface Parser {
		byte[] parse(Object value);
	}
}
