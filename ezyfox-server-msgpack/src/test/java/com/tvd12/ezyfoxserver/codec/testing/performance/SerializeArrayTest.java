package com.tvd12.ezyfoxserver.codec.testing.performance;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackBytesSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.codec.MsgPackToByteBufferSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class SerializeArrayTest extends BaseTest {

	private EzyArray array;
	
	public SerializeArrayTest() {
		EzyEntityFactory.create(EzyArrayBuilder.class);
		EzyArrayBuilder builder = EzyEntityFactory.create(EzyArrayBuilder.class);
		builder.append((Object)null);
		builder.append("");
		builder.append(1);
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("hello", "world")
				.append("foo", "bar"));
		builder.append("i love you");
		builder.append(Lists.newArrayList(1, 2, 3, 4, 5));
		this.array = builder.build();
	}
	
	@Test
	public void test0() {
		EzyMessageSerializer serializer1 = new MsgPackSimpleSerializer();
		EzyMessageSerializer serializer2 = new MsgPackToByteBufferSerializer();
		byte[] array1 = serializer1.serialize(array);
		byte[] array2 = serializer2.serialize(array);
		assertEquals(array1, array2);
	}
	
	@Test
	public void test1() {
		EzyMessageSerializer serializer = new MsgPackBytesSerializer();
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			serializer.serialize(array);
		})
		.getTime();
		System.out.println("SerializeArrayTest::test1 elapsed time = " + time);
	}
	
	@Test
	public void test2() {
		EzyMessageSerializer serializer = new MsgPackToByteBufferSerializer();
		long time = Performance.create()
		.loop(1000000)
		.test(()-> {
			serializer.write(array);
		})
		.getTime();
		System.out.println("SerializeArrayTest::test2 elapsed time = " + time);
	}
	
}
