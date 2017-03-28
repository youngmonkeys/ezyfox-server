package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.io.EzyByteBuffers;

public class JacksonSimpleDeserializer
		extends JacksonObjectMapperSetter
		implements EzyMessageDeserializer {
	
	public JacksonSimpleDeserializer(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] data) {
		return (T) parse(readTree(data));
	}
	
	protected JsonNode readTree(byte[] data) {
		try {
			return objectMapper.readTree(data);
		} catch (Exception e) {
			throw new IllegalArgumentException("read tree error", e);
		} 
	}
	
	protected Object parse(JsonNode node) {
		if(node.isBinary())
			return parseBinary(node);
		else if(node.isArray())
			return parseArray(node);
		else if(node.isObject())
			return parseObject(node);
		else if(node.isBoolean())
			return parseBoolean(node);
		else if(node.isNumber())
			return parseNumber(node);
		return parseText(node);
	}
	
	protected String parseText(JsonNode node) {
		return node.asText();
	}
	
	protected Number parseNumber(JsonNode node) {
		return node.numberValue(); 
	}
	
	protected boolean parseBoolean(JsonNode node) {
		return node.asBoolean();
	}
	
	protected byte[] parseBinary(JsonNode node) {
		try {
			return node.binaryValue();
		} catch (IOException e) {
			throw new IllegalStateException("parse binary error", e);
		}
	}
	
	protected EzyArray parseArray(JsonNode node) {
		EzyArrayBuilder arrayBuilder = newArrayBuilder();
		Iterator<JsonNode> iterator = node.iterator();
		while(iterator.hasNext())
			arrayBuilder.append(parse(iterator.next()));
		return arrayBuilder.build();
	}
	
	protected EzyObject parseObject(JsonNode node) {
		EzyObjectBuilder objectBuilder = newObjectBuilder();
		Iterator<Entry<String, JsonNode>> fields = node.fields();
		while(fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			objectBuilder.append(field.getKey(), parse(field.getValue()));
		}
		return objectBuilder.build();
	}
	
	private EzyArrayBuilder newArrayBuilder() {
		return EzyEntityFactory.create(EzyArrayBuilder.class);
	}
	
	private EzyObjectBuilder newObjectBuilder() {
		return EzyEntityFactory.create(EzyObjectBuilder.class);
	}

	@Override
	public <T> T deserialize(ByteBuffer buffer) {
		return deserialize(EzyByteBuffers.getBytes(buffer));
	}

}
