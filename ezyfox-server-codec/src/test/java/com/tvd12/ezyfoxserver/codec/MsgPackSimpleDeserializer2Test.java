package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class MsgPackSimpleDeserializer2Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		MsgPackSerializer serializer = new MsgPackSimpleSerializer();
		EzyObjectBuilder dataBuilder = newObjectBuilder()
				.append("a", 1)
				.append("b", 2)
				.append("c", (Integer)null)
				.append("d", false)
				.append("e", true)
				.append("f", new byte[] {'a', 'b', 'c'});
		EzyArray request = newArrayBuilder()
				.append(15)
				.append(26)
				.append("abcdef")
				.append(dataBuilder)
				.build();
		byte[] bytes = serializer.serialize(request);
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		getLogger().info("answer = {}", answer);
		int appId = answer.get(0);
		int command = answer.get(1);
		String token = answer.get(2);
		assert appId == 15 : "deserialize error";
		assert command == 26 : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
	}
	
}
