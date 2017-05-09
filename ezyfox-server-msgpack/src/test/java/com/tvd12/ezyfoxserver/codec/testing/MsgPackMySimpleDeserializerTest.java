package com.tvd12.ezyfoxserver.codec.testing;

import java.io.IOException;

import org.msgpack.MessagePack;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyInts;
import com.tvd12.ezyfoxserver.io.EzyLongs;

public class MsgPackMySimpleDeserializerTest extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		EzyMessageSerializer serializer = new MsgPackSimpleSerializer();
		EzyObjectBuilder dataBuilder = newObjectBuilder()
				.append("a", 1)
				.append("b", 2)
				.append("c", (Integer)null)
				.append("d", false)
				.append("e", true)
				.append("f", new byte[] {'a', 'b', 'c'})
				.append("i", EzyInts.bin2int(6))
				.append("j", EzyInts.bin2int(12))
				.append("k", EzyInts.bin2int(18))
				.append("l", EzyLongs.bin2long(34))
				.append("m", -EzyInts.bin2int(6))
				.append("n", -EzyInts.bin2int(12))
				.append("o", -EzyInts.bin2int(18))
				.append("p", -EzyLongs.bin2long(34))
				;
		EzyArray request = newArrayBuilder()
				.append(15)
				.append(26)
				.append("abcdef")
				.append(-1)
				.append(dataBuilder)
				.build();
		long start = System.currentTimeMillis();
		byte[] bytes = serializer.serialize(request);
		long end = System.currentTimeMillis() - start;
		getLogger().info("serialize time elapse = {}", end);
		MessagePack msgPack = new MessagePack();
		start = System.currentTimeMillis();
		msgPack.write(dataBuilder.build().toMap());
		end = System.currentTimeMillis() - start;
		getLogger().info("msg serialize time elapse = {}", end);
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		start = System.currentTimeMillis();
		EzyArray answer = deserializer.deserialize(bytes);
		end = System.currentTimeMillis() - start;
		getLogger().info("deserialize time elapse = {}", end);
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
