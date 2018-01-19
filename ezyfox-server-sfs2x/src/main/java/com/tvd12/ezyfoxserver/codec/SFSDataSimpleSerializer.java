package com.tvd12.ezyfoxserver.codec;

import java.util.Map;

import com.tvd12.ezyfoxserver.function.EzyCastIntToByte;
import com.tvd12.ezyfoxserver.function.EzyParser;
import com.tvd12.ezyfoxserver.io.EzyBytes;
import com.tvd12.ezyfoxserver.io.EzyStrings;

public class SFSDataSimpleSerializer 
	extends EzyAbstractToBytesSerializer 
	implements EzyCastIntToByte {

	@Override
	protected byte[] parseNil() {
		return new byte[1];
	}

	@Override
	protected void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers) {
		parsers.put(Boolean.class, this::parseBool);
	}
	
	protected byte[] parseBool(Object value) {
		return parseBool((Boolean)value);
	}
	
	protected byte[] parseBool(Boolean value) {
		return toBytes(SFSDataType.BOOL, toByte(value));
	}
	
	protected byte[] parseByte(Byte value) {
		return toBytes(SFSDataType.BYTE, value);
	}
	
	protected byte[] parseChar(Character value) {
		return toBytes(SFSDataType.BYTE, (byte)value.charValue());
	}
	
	protected byte[] parseDouble(Double value) {
		return toBytes(SFSDataType.DOUBLE, EzyBytes.getBytes(value));
	}
	
	protected byte[] parseFloat(Float value) {
		return toBytes(SFSDataType.FLOAT, EzyBytes.getBytes(value));
	}
	
	protected byte[] parseInt(Integer value) {
		return toBytes(SFSDataType.INT, EzyBytes.getBytes(value));
	}
	
	protected byte[] parseLong(Long value) {
		return toBytes(SFSDataType.LONG, EzyBytes.getBytes(value));
	}
	
	protected byte[] parseShort(Short value) {
		return toBytes(SFSDataType.SHORT, EzyBytes.getBytes(value));
	}
	
	protected byte[] parseString(String value) {
		return toBytes(SFSDataType.STRING, EzyStrings.getUtfBytes(value));
	}
	
	protected byte[] parseText(String value) {
		return toBytes(SFSDataType.TEXT, EzyStrings.getUtfBytes(value));
	}
	
	protected byte toByte(SFSDataType type) {
		return (byte) type.getId();
	}
	
	protected byte toByte(Boolean value) {
		return (byte) (value ? 1 : 0);
	}
	
	protected byte[] toBytes(SFSDataType type, byte value) {
		return toBytes(type, new byte[] {value});
	}
	
	protected byte[] toBytes(SFSDataType type, byte[] value) {
		return EzyBytes.merge(toByte(type), value);
	}
	
}
