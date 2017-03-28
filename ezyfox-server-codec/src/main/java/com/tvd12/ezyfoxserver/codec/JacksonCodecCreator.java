package com.tvd12.ezyfoxserver.codec;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class JacksonCodecCreator implements EzyCodecCreator {

	protected ObjectMapper objectMapper;
	protected EzyMessageSerializer serializer;
	protected EzyMessageDeserializer deserializer;
	
	public JacksonCodecCreator() {
		this.objectMapper = newObjectMapper();
		this.serializer = newSerializer();
		this.deserializer = newDeserializer();
	}
	
	protected ObjectMapper newObjectMapper() {
		return new ObjectMapper();
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
	public ChannelInboundHandlerAdapter newDecoder() {
		return new JacksonByteToMessageDecoder(deserializer);
	}

}
