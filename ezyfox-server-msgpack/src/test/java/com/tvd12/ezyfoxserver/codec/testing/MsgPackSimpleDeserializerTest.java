package com.tvd12.ezyfoxserver.codec.testing;

import java.io.IOException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyInts;
import com.tvd12.ezyfoxserver.io.EzyLongs;

public class MsgPackSimpleDeserializerTest extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		EzyObjectBuilder dataBuilder = newObjectBuilder()
				.append("a", 1)
				.append("b", 2)
				.append("c", (Integer)null)
				.append("d", false)
				.append("e", true)
				.append("f", new byte[] {'a', 'b', 'c'})
				.append("g", 1) // fix uint
				.append("h", -10)
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
				.append(dataBuilder)
				.build();
		byte[] bytes = msgPack.write(request);
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
