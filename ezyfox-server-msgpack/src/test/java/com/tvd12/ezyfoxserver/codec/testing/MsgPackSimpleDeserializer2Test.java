package com.tvd12.ezyfoxserver.codec.testing;

import java.io.IOException;
import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyMath;

public class MsgPackSimpleDeserializer2Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		byte[] bin32 = new byte[EzyMath.bin2int(16)];
		Arrays.fill(bin32, (byte)'c');
		String str32 = new String(bin32);
		EzyObjectBuilder dataBuilder = newObjectBuilder()
				.append("k", str32);
		EzyArray request = newArrayBuilder()
				.append(15)
				.append(26)
				.append("abcdef")
				.append(dataBuilder)
				.build();
		int arrsize = request.size();
		byte first = (byte)(arrsize | 0x90);
		byte[] bytes = msgPack.write(request);
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		int appId = answer.get(0);
		int command = answer.get(1);
		String token = answer.get(2);
		assert appId == 15 : "deserialize error " + first;
		assert command == 26 : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
	}
	
}
