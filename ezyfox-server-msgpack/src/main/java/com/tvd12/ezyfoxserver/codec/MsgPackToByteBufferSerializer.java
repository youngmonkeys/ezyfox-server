package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;
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
import com.tvd12.ezyfoxserver.io.EzyByteBuffers;
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

public class MsgPackToByteBufferSerializer 
		extends EzyAbstractToByteBufferSerializer 
		implements EzyCastToByte {

	protected IntSerializer intSerializer = new IntSerializer();
	protected FloatSerializer floatSerializer = new FloatSerializer();
	protected DoubleSerializer doubleSerializer = new DoubleSerializer();
	protected BinSizeSerializer binSizeSerializer = new BinSizeSerializer();
	protected MapSizeSerializer mapSizeSerializer = new MapSizeSerializer();
	protected ArraySizeSerializer arraySizeSerializer = new ArraySizeSerializer();
	protected StringSizeSerializer stringSizeSerializer = new StringSizeSerializer();

	@Override
	protected void addParsers(Map<Class<?>, EzyParser<Object, ByteBuffer>> parsers) {
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
	protected ByteBuffer parsePrimitiveBooleans(Object array) {
		return parseBooleans((boolean[])array);
	}
	
	protected ByteBuffer parsePrimitiveChars(Object array) {
		return parseChars((char[])array);
	}
	
	protected ByteBuffer parsePrimitiveDoubles(Object array) {
		return parseDoubles((double[])array);
	}
	
	protected ByteBuffer parsePrimitiveFloats(Object array) {
		return parseFloats((float[])array);
	}
	
	protected ByteBuffer parsePrimitiveInts(Object array) {
		return parseInts((int[])array);
	}
	
	protected ByteBuffer parsePrimitiveLongs(Object array) {
		return parseLongs((long[])array);
	}
	
	protected ByteBuffer parsePrimitiveShorts(Object array) {
		return parseShorts((short[])array);
	}
	
	protected ByteBuffer parseStrings(Object array) {
		return parseStrings((String[])array);
	}
	//
	
	protected ByteBuffer parseBooleans(boolean[] array) {
		return parseArray(EzyBoolsIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseChars(char[] array) {
		return parseBin(EzyDataConverter.charArrayToByteArray(array));
	}
	
	protected ByteBuffer parseDoubles(double[] array) {
		return parseArray(EzyDoublesIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseFloats(float[] array) {
		return parseArray(EzyFloatsIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseInts(int[] array) {
		return parseArray(EzyIntsIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseLongs(long[] array) {
		return parseArray(EzyLongsIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseShorts(short[] array) {
		return parseArray(EzyShortsIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseStrings(String[] array) {
		return parseArray(EzyStringsIterator.wrap(array), array.length);
	}
	
	//=============
	protected ByteBuffer parseWrapperBooleans(Object array) {
		return parseBooleans((Boolean[])array);
	}
	
	protected ByteBuffer parseWrapperBytes(Object array) {
		return parseBytes((Byte[])array);
	}
	
	protected ByteBuffer parseWrapperChars(Object array) {
		return parseChars((Character[])array);
	}
	
	protected ByteBuffer parseWrapperDoubles(Object array) {
		return parseDoubles((Double[])array);
	}
	
	protected ByteBuffer parseWrapperFloats(Object array) {
		return parseFloats((Float[])array);
	}
	
	protected ByteBuffer parseWrapperInts(Object array) {
		return parseInts((Integer[])array);
	}
	
	protected ByteBuffer parseWrapperLongs(Object array) {
		return parseLongs((Long[])array);
	}
	
	protected ByteBuffer parseWrapperShorts(Object array) {
		return parseShorts((Short[])array);
	}
	//
	//=============
	
	//
	protected ByteBuffer parseBooleans(Boolean[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseBytes(Byte[] array) {
		return parseBin(EzyDataConverter.toPrimitiveByteArray(array));
	}
	
	protected ByteBuffer parseChars(Character[] array) {
		return parseBin(EzyDataConverter.charWrapperArrayToPrimitiveByteArray(array));
	}
	
	protected ByteBuffer parseDoubles(Double[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseFloats(Float[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseInts(Integer[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseLongs(Long[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	
	protected ByteBuffer parseShorts(Short[] array) {
		return parseArray(EzyWrapperIterator.wrap(array), array.length);
	}
	//
	
	protected ByteBuffer parseBoolean(Object value) {
		return parseBoolean((Boolean)value);
	}
	
	protected ByteBuffer parseBoolean(Boolean value) {
		return value ? parseTrue() : parseFalse();
	}
	
	protected ByteBuffer parseFalse() {
		return ByteBuffer.wrap(new byte[] {cast(0xc2)});
	}
	
	protected ByteBuffer parseTrue() {
		return ByteBuffer.wrap(new byte[] {cast(0xc3)});
	}
	
	protected ByteBuffer parseByte(Object value) {
		return parseByte((Byte)value);
	}
	
	protected ByteBuffer parseByte(Byte value) {
		return parseInt(value.intValue());
	}
	
	protected ByteBuffer parseChar(Object value) {
		return parseChar((Character)value);
	}
	
	protected ByteBuffer parseChar(Character value) {
		return parseByte((byte)value.charValue());
	}
	
	protected ByteBuffer parseDouble(Object value) {
		return parseDouble((Double)value);
	}
	
	protected ByteBuffer parseDouble(Double value) {
		return ByteBuffer.wrap(doubleSerializer.serialize(value));
	}
	
	protected ByteBuffer parseFloat(Object value) {
		return parseFloat((Float)value);
	}
	
	protected ByteBuffer parseFloat(Float value) {
		return ByteBuffer.wrap(floatSerializer.serialize(value));
	}
	
	protected ByteBuffer parseInt(Object value) {
		return ByteBuffer.wrap(intSerializer.serialize(((Number)value).longValue()));
	}
	
	protected ByteBuffer parseShort(Object value) {
		return parseShort((Short)value);
	}
	
	protected ByteBuffer parseShort(Short value) {
		return parseInt(value.intValue());
	}
	
	protected ByteBuffer parseString(Object string) {
		return parseString((String)string);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected ByteBuffer parseMap(Object map) {
		return parseMap((Map)map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ByteBuffer parseMap(Map map) {
		return parseEntries(map.entrySet());
	}
	
	protected ByteBuffer parseObject(Object obj) {
		return parseEntries(((EzyObject)obj).entrySet());
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected ByteBuffer parseCollection(Object coll) {
		return parseCollection((Collection)coll);
	}
	
	protected ByteBuffer parseArray(Object array) {
		return parseArray((EzyArray)array);
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected ByteBuffer parseCollection(Collection coll) {
		return parseIterable(coll, coll.size());
	}
	
	protected ByteBuffer parseArray(EzyArray array) {
		return parseArray(array.iterator(), array.size());
	}
	
	protected ByteBuffer parseNil() {
		return ByteBuffer.wrap(new byte[] {cast(0xc0)});
	}
	
	protected ByteBuffer parseBin(Object bin) {
		return parseBin((byte[])bin);
	}
	
	protected ByteBuffer parseBin(byte[] bin) {
		ByteBuffer[] buffers = new ByteBuffer[2];
		buffers[0] = ByteBuffer.wrap(parseBinSize(bin.length));
		buffers[1] = ByteBuffer.wrap(bin);
		return EzyByteBuffers.merge(buffers);
	}
	
	protected byte[] parseBinSize(int size) {
		return binSizeSerializer.serialize(size);
	}
	
	protected ByteBuffer parseString(String string) {
		ByteBuffer[] buffers = new ByteBuffer[2];
		buffers[1] = ByteBuffer.wrap(EzyStrings.getUtfBytes(string));
		buffers[0] = ByteBuffer.wrap(parseStringSize(buffers[1].remaining()));
		return EzyByteBuffers.merge(buffers);
	}
	
	protected byte[] parseStringSize(int size) {
		return stringSizeSerializer.serialize(size);
	}
	
	@SuppressWarnings("rawtypes")
	protected ByteBuffer parseIterable(Iterable iterable, int size) {
		return parseArray(iterable.iterator(), size);
	}
	
	@SuppressWarnings("rawtypes")
	protected ByteBuffer parseArray(Iterator iterator, int size) {
		int index = 1;
		ByteBuffer[] buffers = new ByteBuffer[size + 1];
		buffers[0] = ByteBuffer.wrap(parseArraySize(size));
		while(iterator.hasNext())
			buffers[index ++] = write(iterator.next());
		return EzyByteBuffers.merge(buffers);
	}
	
	protected byte[] parseArraySize(int size) {
		return arraySizeSerializer.serialize(size);
	}
	
	protected ByteBuffer parseEntries(Set<Entry<Object, Object>> entries) {
		int index = 1;
		int size = entries.size();
		ByteBuffer[] buffers = new ByteBuffer[size * 2 + 1];
		buffers[0] = ByteBuffer.wrap(parseMapSize(size));
		for(Entry<Object, Object> e : entries) {
			buffers[index++] = write(e.getKey());
			buffers[index++] = write(e.getValue());
		}
		return EzyByteBuffers.merge(buffers);
	}
	
	protected byte[] parseMapSize(int size) {
		return mapSizeSerializer.serialize(size);
	}
	
}