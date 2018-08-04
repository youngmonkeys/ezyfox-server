package com.tvd12.ezyfoxserver.netty.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfox.codec.EzyMessageByTypeSerializer;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfox.codec.JacksonObjectMapperBuilder;
import com.tvd12.ezyfox.codec.JacksonSimpleDeserializer;
import com.tvd12.ezyfox.codec.JacksonSimpleSerializer;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

public class JacksonCodecCreator implements EzyCodecCreator {

	protected final ObjectMapper objectMapper;
	protected final EzyMessageDeserializer deserializer;
	protected final EzyMessageByTypeSerializer serializer;
	
	public JacksonCodecCreator() {
		this.objectMapper = newObjectMapper();
		this.serializer = newSerializer();
		this.deserializer = newDeserializer();
	}
	
	protected ObjectMapper newObjectMapper() {
		return JacksonObjectMapperBuilder.newInstance().build();
	}
	
	protected EzyMessageDeserializer newDeserializer() {
		return new JacksonSimpleDeserializer(objectMapper);
	}
	
	protected EzyMessageByTypeSerializer newSerializer() {
		return new JacksonSimpleSerializer(objectMapper);
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
