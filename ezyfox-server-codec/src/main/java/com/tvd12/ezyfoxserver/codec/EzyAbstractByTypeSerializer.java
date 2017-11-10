package com.tvd12.ezyfoxserver.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.function.EzyParser;
import com.tvd12.ezyfoxserver.io.EzyMaps;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractByTypeSerializer implements EzyMessageByTypeSerializer {

	protected Map<Class<?>, Map<Class<?>, EzyParser>> parserss = defaultParserss();
	
	@Override
	public <T> T serialize(Object value, Class<T> outType) {
		return value == null
				? parseNil(outType)
				: parseNotNull(value, outType);
	}
	
	protected <T> T parseNotNull(Object value, Class<T> outType) {
		Map<Class<?>, EzyParser<Object, Object>> parsers = getParsers(value.getClass());
		if(parsers == null)
			return parseWithNoParsers(value, outType);
		EzyParser<Object, Object> parser = parsers.get(outType);
		if(parser == null)
			return parseWithNoParser(value, outType);
		return parseWithParser(parser, value);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T parseWithParser(EzyParser<Object, Object> parser, Object value) {
		return (T)parser.parse(value);
	}
	
	protected <T> T parseWithNoParsers(Object value, Class<T> outType) {
		throw new IllegalArgumentException("has no parse for " + value.getClass());
	}
	
	protected <T> T parseWithNoParser(Object value, Class<T> outType) {
		throw new IllegalArgumentException("has no parse for " + value.getClass() + " and outType " + outType);
	}
	
	protected abstract <T> T parseNil(Class<T> outType);
	
	protected Map<Class<?>, EzyParser<Object, Object>> getParsers(Class<?> type) {
		return EzyMaps.getValue(parserss, type);
	}
	
	protected Map<Class<?>, Map<Class<?>, EzyParser>> defaultParserss() {
		Map<Class<?>, Map<Class<?>, EzyParser>> map = new ConcurrentHashMap<>();
		addParserss(map);
		return map;
	}
	
	protected abstract void addParserss(Map<Class<?>, Map<Class<?>, EzyParser>> parserss);
	
}
