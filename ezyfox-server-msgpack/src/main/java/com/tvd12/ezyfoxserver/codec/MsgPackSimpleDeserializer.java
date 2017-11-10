package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.function.EzyParser;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyInts;
import com.tvd12.ezyfoxserver.io.EzyLongs;
import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.util.EzyLiteEntityBuilders;

public class MsgPackSimpleDeserializer
		extends EzyLiteEntityBuilders
		implements EzyMessageDeserializer {

	private MsgPackTypeParser typeParser = new MsgPackTypeParser();
	private MapSizeDeserializer mapSizeDeserializer = new MapSizeDeserializer();
	private ArraySizeDeserializer arraySizeDeserializer = new ArraySizeDeserializer();
	private StringSizeDeserializer stringSizeDeserializer = new StringSizeDeserializer();
	private Map<MsgPackType, EzyParser<ByteBuffer, Object>> parsers = defaultParsers();
	
	@SuppressWarnings("unchecked")
	public Object deserialize(ByteBuffer buffer) {
		return deserialize(buffer, buffer.get() & 0xff);
	}
	
	protected Object deserialize(ByteBuffer buffer, int type) {
		updateBufferPosition(buffer);
		return deserialize(buffer, getDataType(type));
	}
	
	protected Object deserialize(ByteBuffer buffer, MsgPackType type) {
		return parsers.get(type).parse(buffer);
	}
	
	protected Map<MsgPackType, EzyParser<ByteBuffer, Object>> defaultParsers() {
		Map<MsgPackType, EzyParser<ByteBuffer, Object>> parsers = new ConcurrentHashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected void addParsers(Map<MsgPackType, EzyParser<ByteBuffer, Object>> parsers) {
		parsers.put(MsgPackType.POSITIVE_FIXINT, this::parsePositiveFixInt); //0K
		parsers.put(MsgPackType.NEGATIVE_FIXINT, this::parseNegativeFixInt); //OK
		parsers.put(MsgPackType.UINT8, this::parseUInt8); //OK
		parsers.put(MsgPackType.UINT16, this::parseUInt16); //OK
		parsers.put(MsgPackType.UINT32, this::parseUInt32); //OK
		parsers.put(MsgPackType.UINT64, this::parseUInt64); //OK
		parsers.put(MsgPackType.INT8, this::parseInt8); //OK
		parsers.put(MsgPackType.INT16, this::parseInt16); //OK
		parsers.put(MsgPackType.INT32, this::parseInt32); //OK
		parsers.put(MsgPackType.INT64, this::parseInt64); //OK
		parsers.put(MsgPackType.FIXMAP, this::parseFixMap); //OK
		parsers.put(MsgPackType.MAP16, this::parseMap16); //OK
		parsers.put(MsgPackType.MAP32, this::parseMap32); //OK
		parsers.put(MsgPackType.FIXARRAY, this::parseFixArray); //OK
		parsers.put(MsgPackType.ARRAY16, this::parseArray16); //OK
		parsers.put(MsgPackType.ARRAY32, this::parseArray32); //OK
		parsers.put(MsgPackType.FIXSTR, this::parseFixStr);
		parsers.put(MsgPackType.STR8, this::parseStr8);
		parsers.put(MsgPackType.STR16, this::parseStr16);
		parsers.put(MsgPackType.STR32, this::parseStr32);
		parsers.put(MsgPackType.NIL, this::parseNil);
		parsers.put(MsgPackType.FALSE, this::parseFalse);
		parsers.put(MsgPackType.TRUE, this::parseTrue);
		parsers.put(MsgPackType.BIN8, this::parseBin8);
		parsers.put(MsgPackType.BIN16, this::parseBin16);
		parsers.put(MsgPackType.BIN32, this::parseBin32);
		parsers.put(MsgPackType.FLOAT32, this::parseFloat32);
		parsers.put(MsgPackType.FLOAT64, this::parseFloat64);
	}
	
	protected Object parseFloat32(ByteBuffer buffer) {
		buffer.get();
		return buffer.getFloat();
	}
	
	protected Object parseFloat64(ByteBuffer buffer) {
		buffer.get();
		return buffer.getDouble();
	}
	
	protected Object parseBin32(ByteBuffer buffer) {
		return parseBin(buffer, getBinLength(buffer, 4));
	}
	
	protected Object parseBin16(ByteBuffer buffer) {
		return parseBin(buffer, getBinLength(buffer, 2));
	}
	
	protected Object parseBin8(ByteBuffer buffer) {
		return parseBin(buffer, getBinLength(buffer, 1));
	}
	
	protected int getBinLength(ByteBuffer buffer, int size) {
		buffer.get();
		return EzyInts.bin2uint(buffer, size);
	}
	
	protected Object parseBin(ByteBuffer buffer, int length) {
		return EzyBytes.copy(buffer, length);
	}
	
	protected Object parseTrue(ByteBuffer buffer) {
		return parseValue(buffer, Boolean.TRUE);
	}
	
	protected Object parseFalse(ByteBuffer buffer) {
		return parseValue(buffer, Boolean.FALSE);
	}
	
	protected Object parseNil(ByteBuffer buffer) {
		return parseValue(buffer, null);
	}
	
	protected Object parseValue(ByteBuffer buffer, Object value) {
		buffer.get();
		return value;
	}
	
	protected EzyObject parseFixMap(ByteBuffer buffer) {
		return parseMap(buffer, getMapSize(buffer, 1));
	}
	
	protected EzyObject parseMap16(ByteBuffer buffer) {
		return parseMap(buffer, getMapSize(buffer, 3));
	}
	
	protected EzyObject parseMap32(ByteBuffer buffer) {
		return parseMap(buffer, getMapSize(buffer, 5));
	}
	
	protected int getMapSize(ByteBuffer buffer, int nbytes) {
		return mapSizeDeserializer.deserialize(buffer, nbytes);
	}
	
	protected EzyObject parseMap(ByteBuffer buffer, int size) {
		EzyObjectBuilder builder = newObjectBuilder();
		for(int i = 0 ; i < size ; i++)
			builder.append(deserialize(buffer), deserialize(buffer));
		return builder.build();
	}
	
	protected String parseStr32(ByteBuffer buffer) {
		return parseString(buffer, 5);
	}
	
	protected String parseStr16(ByteBuffer buffer) {
		return parseString(buffer, 3);
	}
	
	protected String parseStr8(ByteBuffer buffer) {
		return parseString(buffer, 2);
	}
	
	protected String parseFixStr(ByteBuffer buffer) {
		return parseString(buffer, 1);
	}

	protected String parseString(ByteBuffer buffer, int nbytes) {
		return EzyStrings.newUtf(buffer, parseStringSize(buffer, nbytes));
	}
	
	protected int parseStringSize(ByteBuffer buffer, int nbytes) {
		return stringSizeDeserializer.deserialize(buffer, nbytes);
	}
	
	protected int parsePositiveFixInt(ByteBuffer buffer) {
		return (buffer.get() & 0x7F);
	}
	
	protected int parseNegativeFixInt(ByteBuffer buffer) {
		return buffer.get();
	}
	
	protected int parseUInt8(ByteBuffer buffer) {
		return parseUInt(buffer, 1);
	}
	
	protected int parseUInt16(ByteBuffer buffer) {
		return parseUInt(buffer, 2);
	}
	
	protected int parseUInt32(ByteBuffer buffer) {
		return parseUInt(buffer, 4);
	}
	
	protected long parseUInt64(ByteBuffer buffer) {
		return parseULong(buffer, 8);
	}
	
	protected int parseInt8(ByteBuffer buffer) {
		return parseInt(buffer, 1);
	}
	
	protected int parseInt16(ByteBuffer buffer) {
		return parseInt(buffer, 2);
	}
	
	protected int parseInt32(ByteBuffer buffer) {
		return parseInt(buffer, 4);
	}
	
	protected long parseInt64(ByteBuffer buffer) {
		return parseLong(buffer, 8);
	}
	
	protected int parseUInt(ByteBuffer buffer, int size) {
		return (int) parseULong(buffer, size);
	}
	
	protected long parseULong(ByteBuffer buffer, int size) {
		buffer.get();
		return EzyLongs.bin2ulong(buffer, size);
	}
	
	protected int parseInt(ByteBuffer buffer, int size) {
		return (int) parseLong(buffer, size);
	}
	
	protected long parseLong(ByteBuffer buffer, int size) {
		buffer.get();
		return EzyLongs.bin2long(buffer, size);
	}
	
	protected EzyArray parseFixArray(ByteBuffer buffer) {
		return parseArray(buffer, parseArraySize(buffer, 1));
	}
	
	protected EzyArray parseArray16(ByteBuffer buffer) {
		return parseArray(buffer, parseArraySize(buffer, 3));
	}
	
	protected EzyArray parseArray32(ByteBuffer buffer) {
		return parseArray(buffer, parseArraySize(buffer, 5));
	}
	
	protected int parseArraySize(ByteBuffer buffer, int nbytes) {
		return arraySizeDeserializer.deserialize(buffer, nbytes);
	}
	
	protected EzyArray parseArray(ByteBuffer buffer, int size) {
		EzyArrayBuilder builder = newArrayBuilder();
		for(int i = 0 ; i < size ; i++) 
			builder.append(deserialize(buffer));
		return builder.build();
	}
	
	protected MsgPackType getDataType(int type) {
		return typeParser.parse(type);
	}
	
	protected void updateBufferPosition(ByteBuffer buffer) {
		updateBufferPosition(buffer, -1);
	}
	
	protected void updateBufferPosition(ByteBuffer buffer, int offset) {
		buffer.position(buffer.position() + offset);
	}
	
}

abstract class AbstractSizeDeserializer {
	
	public int deserialize(ByteBuffer buffer, int nbytes) {
		return nbytes == 1
				? getFix(buffer)
				: getOther(buffer, nbytes);
	}
	
	protected abstract int getFix(ByteBuffer buffer);
	
	protected int getOther(ByteBuffer buffer, int nbytes) {
		buffer.get();
		return EzyInts.bin2uint(buffer, nbytes - 1);
	}
}

class StringSizeDeserializer extends AbstractSizeDeserializer {
	
	@Override
	protected int getFix(ByteBuffer buffer) {
		return buffer.get() & 0x1F;
	}
	
}

class MapSizeDeserializer extends AbstractSizeDeserializer {
	
	@Override
	protected int getFix(ByteBuffer buffer) {
		return buffer.get() & 0xF;
	}
	
}

class ArraySizeDeserializer extends AbstractSizeDeserializer {
	
	@Override
	protected int getFix(ByteBuffer buffer) {
		return buffer.get() & 0xF;
	}
	
}