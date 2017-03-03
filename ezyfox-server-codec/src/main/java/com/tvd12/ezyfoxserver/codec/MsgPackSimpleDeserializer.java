package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyFactory;
import com.tvd12.ezyfoxserver.util.EzyByteUtil;
import com.tvd12.ezyfoxserver.util.EzyStringUtil;

public class MsgPackSimpleDeserializer implements MsgPackDeserializer {

	private MsgPackTypeParser typeParser;
	protected Map<MsgPackType, Parser> parsers;

	{
		parsers = defaultParsers();
		typeParser = new MsgPackTypeParser();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] data) {
		return (T) deserialize(ByteBuffer.wrap(data));
	}
	
	protected Object deserialize(ByteBuffer buffer) {
		return deserialize(buffer, buffer.get() & 0xff);
	}
	
	protected Object deserialize(ByteBuffer buffer, int type) {
		updateBufferPosition(buffer);
		return deserialize(buffer, getDataType(type));
	}
	
	protected Object deserialize(ByteBuffer buffer, MsgPackType type) {
		Object value = parsers.get(type).parse(buffer);
		System.out.println("paser type = " + type + ", value = " + value);
		return value;
	}
	
	protected Map<MsgPackType, Parser> defaultParsers() {
		Map<MsgPackType, Parser> parsers = new HashMap<>();
		addParsers(parsers);
		return parsers;
	}
	
	protected void addParsers(Map<MsgPackType, Parser> parsers) {
		parsers.put(MsgPackType.POSITIVE_FIXINT, this::parsePositiveInt); //1
		parsers.put(MsgPackType.FIXMAP, this::parseFixMap);
		parsers.put(MsgPackType.FIXARRAY, this::parseFixArray); //2
		parsers.put(MsgPackType.FIXSTR, this::parseFixStr);
		parsers.put(MsgPackType.NIL, this::parseNil);
		parsers.put(MsgPackType.FALSE, this::parseFalse);
		parsers.put(MsgPackType.TRUE, this::parseTrue);
		parsers.put(MsgPackType.BIN8, this::parseBin8);
	}
	
	protected Object parseBin8(ByteBuffer buffer) {
		return parseBin(buffer, getBinLength(buffer, 1));
	}
	
	protected int getBinLength(ByteBuffer buffer, int size) {
		buffer.get();
		int result = 0;
		for(int i = 0 ; i < size ; i++)
			result += buffer.get() & 0xff;
		return result;
	}
	
	protected Object parseBin(ByteBuffer buffer, int length) {
		System.out.println("length = " + length);
		return EzyByteUtil.copy(buffer, length);
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
		return parseMap(buffer, buffer.get() & 0xF);
	}
	
	protected EzyObject parseMap(ByteBuffer buffer, int size) {
		EzyObjectBuilder builder = newObjectBuilder();
		for(int i = 0 ; i < size ; i++)
			builder.append(deserialize(buffer), deserialize(buffer));
		return builder.build();
	}
	
	protected String parseFixStr(ByteBuffer buffer) {
		return parseString(buffer, buffer.get() & 0x1F);
	}

	protected String parseString(ByteBuffer buffer, int size) {
		System.out.println("str.size = " + size);
		return EzyStringUtil.newUTF(buffer, size);
	}
	
	protected int parsePositiveInt(ByteBuffer buffer) {
		return (buffer.get() & 0x7F);
	}
	
	protected EzyArray parseFixArray(ByteBuffer buffer) {
		return parseArray(buffer, buffer.get() & 0x0f);
	}
	
	protected EzyArray parseArray(ByteBuffer buffer, int size) {
		EzyArrayBuilder builder = newArrayBuilder();
		for(int i = 0 ; i < size ; i++) 
			builder.append(deserialize(buffer));
		return builder.build();
	}
	
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyFactory.create(EzyObjectBuilder.class);
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyFactory.create(EzyArrayBuilder.class);
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

interface Parser {
	Object parse(ByteBuffer buffer); 
}