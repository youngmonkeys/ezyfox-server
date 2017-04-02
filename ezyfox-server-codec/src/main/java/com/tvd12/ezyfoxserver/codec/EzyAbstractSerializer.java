package com.tvd12.ezyfoxserver.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.function.EzyParser;
import com.tvd12.ezyfoxserver.util.EzyMaps;

public abstract class EzyAbstractSerializer implements EzyMessageSerializer {

	protected Map<Class<?>, EzyParser<Object, byte[]>> parsers = defaultParsers();
	
	@Override
	public byte[] serialize(Object value) {
		return value == null
				? parseNil()
				: parseNotNull(value);
	}
	
	protected byte[] parseNotNull(Object value) {
		EzyParser<Object, byte[]> parser = getParser(value.getClass());
		if(parser != null)
			return parseWithParser(parser, value);
		return parseWithNoParser(value);
		
	}
	
	protected byte[] parseWithParser(EzyParser<Object, byte[]> parser, Object value) {
		return parser.parse(value);
	}
	
	protected byte[] parseWithNoParser(Object value) {
		throw new IllegalArgumentException("has no parse for " + value.getClass());
	}
	
	protected abstract byte[] parseNil();
	
	protected EzyParser<Object, byte[]> getParser(Class<?> type) {
		return EzyMaps.getValue(parsers, type);
	}
	
	protected Map<Class<?>, EzyParser<Object, byte[]>> defaultParsers() {
		Map<Class<?>, EzyParser<Object, byte[]>> parsers = new ConcurrentHashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected abstract void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers);
	
}
