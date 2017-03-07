package com.tvd12.ezyfoxserver.codec;

import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.CastIntToByte;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyStrings;

public class MsgPackSimpleSerializer implements MsgPackSerializer, CastIntToByte {

	protected Map<Class<?>, Parser> parsers;
	
	private IntSerializer intSerializer;
	private BinSizeSerializer binSizeSerializer;
	private MapSizeSerializer mapSizeSerializer;
	private ArraySizeSerializer arraySizeSerializer;
	private StringSizeSerializer stringSizeSerializer;

	{
		parsers = defaultParsers();
		intSerializer = new IntSerializer();
		binSizeSerializer = new BinSizeSerializer();
		mapSizeSerializer = new MapSizeSerializer();
		arraySizeSerializer = new ArraySizeSerializer();
		stringSizeSerializer = new StringSizeSerializer();
	}
	
	@Override
	public byte[] serialize(Object value) {
		return value == null
				? parseNil()
				: parseNotNull(value);
	}
	
	protected byte[] parseNotNull(Object value) {
		Parser parser = getParser(value.getClass());
		if(parser != null)
			return parser.parse(value);
		throw new IllegalArgumentException("has no parse for " + value.getClass());
		
	}
	
	protected Parser getParser(Class<?> type) {
		if(type == Object.class)
			return null;
		Parser parser = parsers.get(type);
		if(parser == null && type.getInterfaces() != null)
			parser = getParserOfInterfaces(type);
		if(parser == null && type.getSuperclass() != null)
			parser = getParserOfSuper(type);
		return parser;
	}
	
	protected Parser getParserOfSuper(Class<?> type) {
		return getParser(type.getSuperclass());
	}
	
	protected Parser getParserOfInterfaces(Class<?> type) {
		Parser answer = null;
		for(Class<?> clazz : type.getInterfaces())
			if((answer = getParser(clazz)) != null)
				return answer;
		return answer;
	}
	
	protected Map<Class<?>, Parser> defaultParsers() {
		Map<Class<?>, Parser> parsers = new HashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected void addParsers(Map<Class<?>, Parser> parsers) {
		parsers.put(Long.class, this::parseInt);
		parsers.put(Integer.class, this::parseInt);
		parsers.put(Map.class, this::parseMap);
		parsers.put(EzyObject.class, this::parseObject);
		parsers.put(EzyArray.class, this::parseArray);
		parsers.put(String.class, this::parseString);
		parsers.put(Boolean.class, this::parseBoolean);
		parsers.put(byte[].class, this::parseBin);
	}
	
	protected byte[] parseInt(Object value) {
		return intSerializer.serialize(((Number)value).longValue());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected byte[] parseMap(Object map) {
		return parseMap(((Map)map).entrySet());
	}
	
	protected byte[] parseObject(Object obj) {
		return parseMap(((EzyObject)obj).entrySet());
	}
	
	protected byte[] parseArray(Object array) {
		return parseArray((EzyArray)array);
	}
	
	protected byte[] parseArray(EzyArray array) {
		return parseArray(array.iterator(), array.size());
	}
	
	protected byte[] parseString(Object string) {
		return parseString((String)string);
	}
	
	protected byte[] parseNil() {
		return new byte[] {cast(0xc0)};
	}
	
	protected byte[] parseBoolean(Object value) {
		return parseBoolean((Boolean)value);
	}
	
	protected byte[] parseBoolean(Boolean value) {
		return value ? parseTrue() : parseFalse();
	}
	
	protected byte[] parseFalse() {
		return new byte[] {cast(0xc2)};
	}
	
	protected byte[] parseTrue() {
		return new byte[] {cast(0xc3)};
	}
	
	protected byte[] parseBin(Object bin) {
		return parseBin((byte[])bin);
	}
	
	protected byte[] parseBin(byte[] bin) {
		byte[][] bytess = new byte[2][];
		bytess[0] = parseBinSize(bin.length);
		bytess[1] = bin;
		return EzyBytes.copy(bytess);
	}
	
	protected byte[] parseBinSize(int size) {
		return binSizeSerializer.serialize(size);
	}
	
	protected byte[] parseString(String string) {
		int size = string.length();
		byte[][] bytess = new byte[2][];
		bytess[0] = parseStringSize(size);
		bytess[1] = EzyStrings.getUTFBytes(string);
		return EzyBytes.copy(bytess);
	}
	
	protected byte[] parseStringSize(int size) {
		return stringSizeSerializer.serialize(size);
	}
	
	protected byte[] parseArray(Iterator<Object> iterator, int size) {
		int index = 1;
		byte[][] bytess = new byte[size + 1][];
		bytess[0] = parseArraySize(size);
		while(iterator.hasNext())
			bytess[index ++] = serialize(iterator.next());
		return EzyBytes.copy(bytess);
	}
	
	protected byte[] parseArraySize(int size) {
		return arraySizeSerializer.serialize(size);
	}
	
	protected byte[] parseMap(Set<Entry<Object, Object>> entries) {
		int index = 1;
		int size = entries.size();
		byte[][] bytess = new byte[size * 2 + 1][];
		bytess[0] = parseMapSize(size);
		for(Entry<Object, Object> e : entries) {
			bytess[index++] = serialize(e.getKey());
			bytess[index++] = serialize(e.getValue());
		}
		return EzyBytes.copy(bytess);
	}
	
	protected byte[] parseMapSize(int size) {
		return mapSizeSerializer.serialize(size);
	}
	
	private static interface Parser {
		byte[] parse(Object value);
	}
}

class BinSizeSerializer implements CastIntToByte {
	
	public byte[] serialize(int size) {
		if(size <= MAX_BIN8_SIZE)
			return parse8(size);
		if(size <= MAX_BIN16_SIZE)
			return parse16(size);
		return parse32(size);
	}
	
	private byte[] parse8(int size) {
		return new byte[] {
			cast(0xc4), cast(size)
		};
	}
	
	private byte[] parse16(int size) {
		return EzyBytes.getBytes(0xc5 , size, 2);
	}
	
	private byte[] parse32(int size) {
		return EzyBytes.getBytes(0xc6 , size, 4);
	}
}

class StringSizeSerializer implements CastIntToByte {
	
	public byte[] serialize(int size) {
		if(size <= MAX_FIXSTR_SIZE)
			return parseFix(size);
		if(size <= MAX_STR8_SIZE)
			return parse8(size);
		if(size <= MAX_STR16_SIZE)
			return parse16(size);
		return parse32(size);
	}
	
	private byte[] parseFix(int size) {
		return new byte[] {
			cast(0xa0 | size)
		};
	}
	
	private byte[] parse8(int size) {
		return EzyBytes.getBytes(0xd9, size, 1);
	}
	
	private byte[] parse16(int size) {
		return EzyBytes.getBytes(0xda, size, 2);
	}
	
	private byte[] parse32(int size) {
		return EzyBytes.getBytes(0xdb, size, 4);
	}
}

class ArraySizeSerializer implements CastIntToByte {
	
	public byte[] serialize(int size) {
		if(size <= MAX_FIXARRAY_SIZE)
			return parseFix(size);
		return new byte[0];
	}
	
	private byte[] parseFix(int size) {
		return new byte[] {
			cast(0x90 | size)
		};
	}
}

class MapSizeSerializer implements CastIntToByte {
	
	public byte[] serialize(int size) {
		if(size <= MAX_FIXMAP_SIZE)
			return parseFix(size);
		if(size <= MAX_MAP16_SIZE)
			return parse16(size);
		return parse32(size);
	}
	
	private byte[] parseFix(int size) {
		return new byte[] {
			cast(0x80 | size)
		};
	}
	
	private byte[] parse16(int size) {
		return EzyBytes.getBytes(0xde, size, 2);
	}
	
	private byte[] parse32(int size) {
		return EzyBytes.getBytes(0xde, size, 2);
	}
}

class IntSerializer implements CastIntToByte {
	
	public byte[] serialize(long value) {
		return value > 0 
				? parsePositive(value) 
				: parseNegative(value);
	}
	
	private byte[] parsePositive(long value) {
		if(value <= MAX_POSITIVE_FIXINT)
			return parsePositiveFix(value);
		if(value < MAX_UINT8)
			return parseU8(value);
		if(value < MAX_BIN16_SIZE)
			return parseU16(value);
		if(value < MAX_BIN32_SIZE)
			return parseU32(value);
		return parseU64(value);
	}
	
	private byte[] parsePositiveFix(long value) {
		return new byte[] {cast(0x0 | value)};
	}
	
	private byte[] parseU8(long value) {
		return EzyBytes.getBytes(0xcc, value, 1);
	}
	
	private byte[] parseU16(long value) {
		return EzyBytes.getBytes(0xcd, value, 2);
	}
	
	private byte[] parseU32(long value) {
		return EzyBytes.getBytes(0xce, value, 4);
	}
	
	private byte[] parseU64(long value) {
		return EzyBytes.getBytes(0xcf, value, 8);
	}
	
	private byte[] parseNegative(long value) {
		if(value < MIN_NEGATIVE_FIXINT)
			return parseNegativeFix(value);
		if(value < MIN_INT8)
			return parse8(value);
		if(value < MIN_INT16)
			return parse16(value);
		if(value < MIN_INT32)
			return parse32(value);
		return parse64(value);
	}
	
	private byte[] parseNegativeFix(long value) {
		return new byte[] {cast(0xe0 | value)};
	}
	
	private byte[] parse8(long value) {
		return EzyBytes.getBytes(0xd0, value, 1);
	}
	
	private byte[] parse16(long value) {
		return EzyBytes.getBytes(0xd1, value, 2);
	}
	
	private byte[] parse32(long value) {
		return EzyBytes.getBytes(0xd2, value, 4);
	}
	
	private byte[] parse64(long value) {
		return EzyBytes.getBytes(0xd3, value, 8);
	}
}