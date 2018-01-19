package com.tvd12.ezyfoxserver.codec.testing.performance;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class DeserializeArrayTest extends BaseTest {

	@Test
	public void test1() {
		EzyEntityFactory.create(EzyArrayBuilder.class);
		EzyArrayBuilder builder = EzyEntityFactory.create(EzyArrayBuilder.class);
		builder.append((Object)null);
		builder.append("");
		builder.append(1);
		EzyArray array = builder.build();
		EzyMessageSerializer serializer = new MsgPackSimpleSerializer();
		EzyMessageDeserializer deserializer = new MsgPackSimpleDeserializer();
		byte[] bytes = serializer.serialize(array);
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			deserializer.deserialize(bytes);
		})
		.getTime();
		System.out.println("DeserializeArrayTest::test1 elapsed time = " + time);
	}
	
}
