package com.tvd12.ezyfoxserver.netty.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.JacksonObjectMapperBuilder;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleSerializer;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class JacksonCodecCreator implements EzyCodecCreator {

	protected final ObjectMapper objectMapper;
	protected final EzyMessageSerializer serializer;
	protected final EzyMessageDeserializer deserializer;
	
	public JacksonCodecCreator() {
		this.objectMapper = newObjectMapper();
		this.serializer = newSerializer();
		this.deserializer = newDeserializer();
	}
	
	protected ObjectMapper newObjectMapper() {
		return JacksonObjectMapperBuilder.newInstance().build();
	}
	
	protected EzyMessageSerializer newSerializer() {
		return new JacksonSimpleSerializer(objectMapper);
	}
	
	protected EzyMessageDeserializer newDeserializer() {
		return new JacksonSimpleDeserializer(objectMapper);
	}
	
	@Override
	public ChannelOutboundHandler newEncoder() {
		return new JacksonMessageToByteEncoder(serializer);
	}

	@Override
	public ChannelInboundHandlerAdapter newDecoder(int maxRequestSize) {
		return new JacksonByteToMessageDecoder(deserializer);
	}

}
