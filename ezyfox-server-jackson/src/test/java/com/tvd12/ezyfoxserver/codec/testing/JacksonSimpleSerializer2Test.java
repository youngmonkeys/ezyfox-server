package com.tvd12.ezyfoxserver.codec.testing;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageByTypeSerializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.JacksonObjectMapperBuilder;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class JacksonSimpleSerializer2Test extends BaseTest {

	private ObjectMapper objectMapper = newObjectMapper();
	private EzyMessageByTypeSerializer serializer 
				= new JacksonSimpleSerializer(objectMapper);
	private EzyMessageDeserializer deserializer
				= new JacksonSimpleDeserializer(objectMapper);
	
	@SuppressWarnings("unchecked")
	@Test
	public void test() throws JsonProcessingException {
		EzyArray origin = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append("a", 1)
				.append("b", 2)
				.append("c", 3)
				.append(EzyEntityFactory.create(EzyArrayBuilder.class))
				.build();
		byte[] bytes = serializer.serialize(origin);
		EzyArray array = deserializer.deserialize(bytes);
		System.out.println(array);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void test2() {
		EzyArray origin = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append("a", 1)
				.append("b", 2)
				.append("c", 3)
				.append(EzyEntityFactory.create(EzyArrayBuilder.class))
				.build();
		long time = Performance.create()
			.loop(100000)
			.test(() -> {
				byte[] bytes = serializer.serialize(origin);
				String text = serializer.serialize(origin, String.class);
				ByteBuffer buffer = serializer.serialize(origin, ByteBuffer.class);
			})
			.getTime();
		System.out.println("test2 time = : " + time);
	}
	
	private ObjectMapper newObjectMapper() {
		return JacksonObjectMapperBuilder.newInstance().build();
	}
	
}
