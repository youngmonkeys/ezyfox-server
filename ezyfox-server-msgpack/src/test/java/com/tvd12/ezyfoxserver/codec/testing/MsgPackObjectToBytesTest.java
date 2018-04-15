package com.tvd12.ezyfoxserver.codec.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackObjectToBytes;
import com.tvd12.test.base.BaseTest;

public class MsgPackObjectToBytesTest extends BaseTest {

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test() {
		MsgPackObjectToBytes.Builder builder = 
				MsgPackObjectToBytes.builder();
		builder.toString();
		MsgPackObjectToBytes.builder()
			.serializer(new EzyMessageSerializer() {
				
				@Override
				public byte[] serialize(Object value) {
					throw new RuntimeException();
				}
			})
			.build()
			.convert(null);
	}
	
}
