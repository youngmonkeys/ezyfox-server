package com.tvd12.ezyfoxserver.codec;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyByteBuffers;
import com.tvd12.ezyfoxserver.util.EzyLiteEntityBuilders;

public class JacksonSimpleDeserializer
		extends EzyLiteEntityBuilders
		implements EzyMessageDeserializer {
	
	protected ObjectMapper objectMapper;
	
	public JacksonSimpleDeserializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public <T> T deserialize(ByteBuffer buffer) {
		return deserialize(EzyByteBuffers.getBytes(buffer));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] data) {
		return (T) parse(readTree(data));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(String text) {
		return (T) parse(readTree(text));
	}
	
	protected JsonNode readTree(byte[] data) {
		try {
			return objectMapper.readTree(data);
		} catch (Exception e) {
			throw new IllegalArgumentException("read tree error", e);
		} 
	}
	
	protected JsonNode readTree(String text) {
		try {
			return objectMapper.readTree(text);
		} catch (Exception e) {
			throw new IllegalArgumentException("read tree error", e);
		} 
	}
	
	protected Object parse(JsonNode node) {
		if(node.isArray())
			return parseArray(node);
		if(node.isObject())
			return parseObject(node);
		if(node.isBoolean())
			return parseBoolean(node);
		if(node.isNumber())
			return parseNumber(node);
		if(node.isNull())
			return null;
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
	
}
