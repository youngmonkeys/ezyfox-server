package com.tvd12.ezyfoxserver.codec.testing;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyMath;

public class MsgPackSimpleDeserializer4Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		int size = EzyMath.bin2int(16);
		EzyArrayBuilder builder = newArrayBuilder();
		for(int i = 0 ; i < size ; i++)
//		for(int i = 1 ; i < 500 ; i++) 
			builder.append(i);
//		builder.append(300);
		byte[] bytes = msgPack.write(builder.build());
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		Assert.assertEquals(answer.get(size - 1), new Integer(size - 1));
	}
	
}
