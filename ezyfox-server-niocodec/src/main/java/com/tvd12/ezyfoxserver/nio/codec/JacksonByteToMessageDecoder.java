package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsByteToObjectDecoder;

public class JacksonByteToMessageDecoder implements EzyWsByteToObjectDecoder {

	private final EzyMessageDeserializer deserializer;
	
	public JacksonByteToMessageDecoder(EzyMessageDeserializer deserializer) {
		this.deserializer = deserializer;
	}
	
	@Override
	public Object decode(String bytes) throws Exception {
		return deserializer.deserialize(bytes);
	}
	
	@Override
	public Object decode(ByteBuffer bytes) throws Exception {
		return deserializer.deserialize(bytes);
	}
	
}
