package com.tvd12.ezyfoxserver.codec.testing;

import java.io.IOException;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyMath;
import static org.testng.Assert.*;

public class MsgPackMySimpleDeserializer4Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		int size = EzyMath.bin2int(14);
		EzyArrayBuilder builder = newArrayBuilder();
		for(int i = 0 ; i < size ; i++)
//		for(int i = 1 ; i < 500 ; i++) 
			builder.append(i);
//		builder.append(300);
		EzyMessageSerializer serializer = new MsgPackSimpleSerializer();
		byte[] bytes = serializer.serialize(builder.build());
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		assertEquals(answer.get(size - 1), new Integer(size - 1));
	}
	
}
