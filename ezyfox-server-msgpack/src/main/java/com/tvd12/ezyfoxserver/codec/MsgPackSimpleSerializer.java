package com.tvd12.ezyfoxserver.codec;

import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_ARRAY16_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_BIN16_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_BIN32_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_BIN8_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_FIXARRAY_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_FIXMAP_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_FIXSTR_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_MAP16_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_POSITIVE_FIXINT;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_STR16_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_STR8_SIZE;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MAX_UINT8;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MIN_INT16;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MIN_INT32;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MIN_INT8;
import static com.tvd12.ezyfoxserver.codec.MsgPackConstant.MIN_NEGATIVE_FIXINT;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyParser;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyCastToByte;
import com.tvd12.ezyfoxserver.io.EzyDataConverter;
import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.util.EzyBoolsIterator;
import com.tvd12.ezyfoxserver.util.EzyDoublesIterator;
import com.tvd12.ezyfoxserver.util.EzyFloatsIterator;
import com.tvd12.ezyfoxserver.util.EzyIntsIterator;
import com.tvd12.ezyfoxserver.util.EzyLongsIterator;
import com.tvd12.ezyfoxserver.util.EzyShortsIterator;
import com.tvd12.ezyfoxserver.util.EzyStringsIterator;
import com.tvd12.ezyfoxserver.util.EzyWrapperIterator;

public class MsgPackSimpleSerializer 
		extends EzyAbstractToBytesSerializer 
		implements EzyCastToByte {

	protected IntSerializer intSerializer = new IntSerializer();
	protected FloatSerializer floatSerializer = new FloatSerializer();
	protected DoubleSerializer doubleSerializer = new DoubleSerializer();
	protected BinSizeSerializer binSizeSerializer = new BinSizeSerializer();
	protected MapSizeSerializer mapSizeSerializer = new MapSizeSerializer();
	protected ArraySizeSerializer arraySizeSerializer = new ArraySizeSerializer();
	protected StringSizeSerializer stringSizeSerializer = new StringSizeSerializer();

	@Override
	protected void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers) {
		parsers.put(Boolean.class, this::parseBoolean);
		parsers.put(Byte.class, this::parseByte);
		parsers.put(Character.class, this::parseChar);
		parsers.put(Double.class, this::parseDouble);
		parsers.put(Float.class, this::parseFloat);
		parsers.put(Integer.class, this::parseInt);
		parsers.put(Long.class, this::parseInt);
		parsers.put(Short.class, this::parseShort);
		parsers.put(String.class, this::parseString);
		
		parsers.put(boolean[].class, this::parsePrimitiveBooleans);
		parsers.put(byte[].class, this::parseBin);
		parsers.put(char[].class, this::parsePrimitiveChars);
		parsers.put(double[].class, this::parsePrimitiveDoubles);
		parsers.put(float[].class, this::parsePrimitiveFloats);
		parsers.put(int[].class, this::parsePrimitiveInts);
		parsers.put(long[].class, this::parsePrimitiveLongs);
		parsers.put(short[].class, this::parsePrimitiveShorts);
		parsers.put(String[].class, this::parseStrings);
		
		parsers.put(Byte[].class, this::parseWrapperBytes);
		parsers.put(Boolean[].class, this::parseWrapperBooleans);
		parsers.put(Character[].class, this::parseWrapperChars);
		parsers.put(Double[].class, this::parseWrapperDoubles);
		parsers.put(Float[].class, this::parseWrapperFloats);
		parsers.put(Integer[].class, this::parseWrapperInts);
		parsers.put(Long[].class, this::parseWrapperLongs);
		parsers.put(Short[].class, this::parseWrapperShorts);
		
		parsers.put(Map.class, this::parseMap);
		parsers.put(AbstractMap.class, this::parseMap);
		parsers.put(EzyObject.class, this::parseObject);
		parsers.put(EzyArray.class, this::parseArray);
		parsers.put(Collection.class, this::parseCollection);
		parsers.put(Set.class, this::parseCollection);
		parsers.put(List.class, this::parseCollection);
		parsers.put(HashSet.class, this::parseCollection);
		parsers.put(ArrayList.class, this::parseCollection);
	}
	
	//
	protected byte[] parsePrimitiveBooleans(Object array) {
		return parseBooleans((boolean[])array);
	}
	
	protected byte[] parsePrimitiveChars(Object array) {
		return parseChars((char[])array);
	}
	
	protected byte[] parsePrimitiveDoubles(Object array) {
		return parseDoubles((double[])array);
	}
	
	protected byte[] parsePrimitiveFloats(Object array) {
		return parseFloats((float[])array);
	}
	
	protected byte[] parsePrimitiveInts(Object array) {
		return parseInts((int[])array);
	}
	
	protected byte[] parsePrimitiveLongs(Object array) {
		return parseLongs((long[])array);
	}
	
	protected byte[] parsePrimitiveShorts(Object array) {
		return parseShorts((short[])array);
	}
	
	protected byte[] parseStrings(Object array) {
		return parseStrings((String[])array);
	}
	//
	
	protected byte[] parseBooleans(boolean[] array) {
		return parseArray(EzyBoolsIterator.wrap(array), array.length);
	}
	
	protected byte[] parseChars(char[] array) {
		return parseBin(EzyDataConverter.charArrayToByteArray(array));
	}
	
	protected byte[] parseDoubles(double[] array) {
		return parseArray(EzyDoublesIterator.wrap(array), array.length);
	}
	
	protected byte[] parseFloats(float[] array) {
		return parseArray(EzyFloatsIterator.wrap(array), array.length);
	}
	
	protected byte[] parseInts(int[] array) {
		return parseArray(EzyIntsIterator.wrap(array), array.length);
	}
	
	protected byte[] parseLongs(long[] array) {
		return parseArray(EzyLongsIterator.wrap(array), array.length);
	}
	
	protected byte[] parseShorts(short[] array) {
		return parseArray(EzyShortsIterator.wrap(array), array.length);
	}
	
	protected byte[] parseStrings(String[] array) {
		return parseArray(EzyStringsIterator.wrap(array), array.length);
	}
	
	//=============
	protected byte[] parseWrapperBooleans(Object array) {
		return parseBooleans((Boolean[])array);
	}
	
	protected byte[] parseWrapperBytes(Object array) {
		return parseBytes((Byte[])array);
	}
	
	protected byte[] parseWrapperChars(Object array) {
		return parseChars((Character[])array);
	}
	
	protected byte[] parseWrapperDoubles(Object array) {
		return parseDoubles((Double[])array);
	}
	
	protected byte[] parseWrapperFloats(Object array) {
		return parseFloats((Float[])array);
	}
	
	protected byte[] parseWrapperInts(Object array) {
		return parseInts((Integer[])array);
	}
	
	protected byte[] parseWrapperLongs(Object array) {
		return parseLongs((Long[])array);
	}
	
	protected byte[] parseWrapperShorts(Object array) {
		return parseShorts((Short[])array);
	}
	//
	//=============
	
	//
	protected byte[] parseBooleans(Boolean[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected byte[] parseBytes(Byte[] array) {
		return parseBin(EzyDataConverter.toPrimitiveByteArray(array));
	}
	
	protected byte[] parseChars(Character[] array) {
		return parseBin(EzyDataConverter.charWrapperArrayToPrimitiveByteArray(array));
	}
	
	protected byte[] parseDoubles(Double[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected byte[] parseFloats(Float[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected byte[] parseInts(Integer[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected byte[] parseLongs(Long[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected byte[] parseShorts(Short[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	//
	
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
	
	protected byte[] parseByte(Object value) {
		return parseByte((Byte)value);
	}
	
	protected byte[] parseByte(Byte value) {
		return parseInt(value.intValue());
	}
	
	protected byte[] parseChar(Object value) {
		return parseChar((Character)value);
	}
	
	protected byte[] parseChar(Character value) {
		return parseByte((byte)value.charValue());
	}
	
	protected byte[] parseDouble(Object value) {
		return parseDouble((Double)value);
	}
	
	protected byte[] parseDouble(Double value) {
		return doubleSerializer.serialize(value);
	}
	
	protected byte[] parseFloat(Object value) {
		return parseFloat((Float)value);
	}
	
	protected byte[] parseFloat(Float value) {
		return floatSerializer.serialize(value);
	}
	
	protected byte[] parseInt(Object value) {
		return intSerializer.serialize(((Number)value).longValue());
	}
	
	protected byte[] parseShort(Object value) {
		return parseShort((Short)value);
	}
	
	protected byte[] parseShort(Short value) {
		return parseInt(value.intValue());
	}
	
	protected byte[] parseString(Object string) {
		return parseString((String)string);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected byte[] parseMap(Object map) {
		return parseMap((Map)map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected byte[] parseMap(Map map) {
		return parseEntries(map.entrySet());
	}
	
	protected byte[] parseObject(Object obj) {
		return parseEntries(((EzyObject)obj).entrySet());
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected byte[] parseCollection(Object coll) {
		return parseCollection((Collection)coll);
	}
	
	protected byte[] parseArray(Object array) {
		return parseArray((EzyArray)array);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected byte[] parseCollection(Collection coll) {
		return parseIterable(coll, coll.size());
	}
	
	protected byte[] parseArray(EzyArray array) {
		return parseArray(array.iterator(), array.size());
	}
	
	protected byte[] parseNil() {
		return new byte[] {cast(0xc0)};
	}
	
	protected byte[] parseBin(Object bin) {
		return parseBin((byte[])bin);
	}
	
	protected byte[] parseBin(byte[] bin) {
		byte[][] bytess = new byte[2][];
		bytess[0] = parseBinSize(bin.length);
		bytess[1] = bin;
		return EzyBytes.merge(bytess);
	}
	
	protected byte[] parseBinSize(int size) {
		return binSizeSerializer.serialize(size);
	}
	
	protected byte[] parseString(String string) {
		byte[][] bytess = new byte[2][];
		bytess[1] = EzyStrings.getUtfBytes(string);
		bytess[0] = parseStringSize(bytess[1].length);
		return EzyBytes.merge(bytess);
	}
	
	protected byte[] parseStringSize(int size) {
		return stringSizeSerializer.serialize(size);
	}
	
	@SuppressWarnings("rawtypes")
	protected byte[] parseIterable(Iterable iterable, int size) {
		return parseArray(iterable.iterator(), size);
	}
	
	@SuppressWarnings("rawtypes")
	protected byte[] parseArray(Iterator iterator, int size) {
		int index = 1;
		byte[][] bytess = new byte[size + 1][];
		bytess[0] = parseArraySize(size);
		while(iterator.hasNext())
			bytess[index ++] = serialize(iterator.next());
		return EzyBytes.merge(bytess);
	}
	
	protected byte[] parseArraySize(int size) {
		return arraySizeSerializer.serialize(size);
	}
	
	protected byte[] parseEntries(Set<Entry<Object, Object>> entries) {
		int index = 1;
		int size = entries.size();
		byte[][] bytess = new byte[size * 2 + 1][];
		bytess[0] = parseMapSize(size);
		for(Entry<Object, Object> e : entries) {
			bytess[index++] = serialize(e.getKey());
			bytess[index++] = serialize(e.getValue());
		}
		return EzyBytes.merge(bytess);
	}
	
	protected byte[] parseMapSize(int size) {
		return mapSizeSerializer.serialize(size);
	}
	
}

class BinSizeSerializer implements EzyCastToByte {
	
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

class StringSizeSerializer implements EzyCastToByte {
	
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

class ArraySizeSerializer implements EzyCastToByte {
	
	public byte[] serialize(int size) {
		if(size <= MAX_FIXARRAY_SIZE)
			return parseFix(size);
		if(size <= MAX_ARRAY16_SIZE)
			return parse16(size);
		return parse32(size);
	}
	
	private byte[] parseFix(int size) {
		return new byte[] {
			cast(0x90 | size)
		};
	}
	
	private byte[] parse16(int size) {
		return EzyBytes.getBytes(0xdc, size, 2);
	}
	
	private byte[] parse32(int size) {
		return EzyBytes.getBytes(0xdd, size, 4);
	}
}

class MapSizeSerializer implements EzyCastToByte {
	
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
		return EzyBytes.getBytes(0xdf, size, 4);
	}
}

class IntSerializer implements EzyCastToByte {
	
	public byte[] serialize(long value) {
		return value >= 0 
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
		if(value >= MIN_NEGATIVE_FIXINT)
			return parseNegativeFix(value);
		if(value >= MIN_INT8)
			return parse8(value);
		if(value >= MIN_INT16)
			return parse16(value);
		if(value >= MIN_INT32)
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

class DoubleSerializer implements EzyCastToByte {
	
	public byte[] serialize(double value) {
		return EzyBytes.getBytes(cast(0xcb), value);
	}
	
}

class FloatSerializer implements EzyCastToByte {
	
	public byte[] serialize(float value) {
		return EzyBytes.getBytes(cast(0xca), value);
	}
	
}