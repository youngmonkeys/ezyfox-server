package com.tvd12.ezyfoxserver.codec.testing;

import java.nio.ByteBuffer;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleDeserializer;
import com.tvd12.test.base.BaseTest;

public class JacksonSimpleDeserializerTest extends BaseTest {

	private ObjectMapper objectMapper 
			= new ObjectMapper();
	private EzyMessageDeserializer deserializer 
			= new JacksonSimpleDeserializer(objectMapper);
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test() {
		deserializer.deserialize(ByteBuffer.wrap(new byte[] {1, 2, 3}));
	}
	
}
