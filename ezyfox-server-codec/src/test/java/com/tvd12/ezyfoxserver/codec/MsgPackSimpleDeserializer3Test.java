package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyMath;

public class MsgPackSimpleDeserializer3Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		EzyArrayBuilder builder = newArrayBuilder();
		for(int i = 0 ; i < EzyMath.bin2int(14) ; i++)
//		for(int i = 1 ; i < 500 ; i++) 
			builder.append(i);
//		builder.append(300);
		byte[] bytes = msgPack.write(builder.build());
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		getLogger().info("answer = {}", answer);
	}
	
}
